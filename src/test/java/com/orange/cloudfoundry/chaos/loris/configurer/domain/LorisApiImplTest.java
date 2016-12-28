package com.orange.cloudfoundry.chaos.loris.configurer.domain;

import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfigurationBuilder;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateChaosResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateScheduleResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Chaos;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client.ApplicationClient;
import com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client.ChaosesClient;
import com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client.ScheduledClient;
import com.orange.cloudfoundry.chaos.loris.configurer.test.ApplicationResourceHelper;
import com.orange.cloudfoundry.chaos.loris.configurer.test.Commons;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.Link;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.orange.cloudfoundry.chaos.loris.configurer.test.ChaosResourceHelper.generateChaosPagedResource;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by O. Orand on 06/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class LorisApiImplTest {

    @Mock
    ChaosesClient chaosesClient;

    @Mock
    ScheduledClient scheduledClient;

    @Mock
    ApplicationClient applicationClient;

    @InjectMocks
    LorisApiImpl lorisApi;
    private String BASE_URL;


    @Test
    public void reset_single_page_Chaoses() throws Exception {
        final int pageSize = 1;
        final int totalPages = 1;
        when(chaosesClient.getAll(anyInt(), anyInt())).thenReturn(
                generateChaosPagedResource(0, totalPages, pageSize),
                generateChaosPagedResource(0, 0, pageSize)
        );

        lorisApi.setPageSize(pageSize);

        lorisApi.resetChaoses();

        verify(chaosesClient, times(totalPages + 1)).getAll(0, pageSize);
        verify(chaosesClient, times(totalPages)).delete(any());
    }

    @Test
    public void reset_multiple_page_Chaoses() throws Exception {
        final int totalPages = 3;
        final int pageSize = 1;

        when(chaosesClient.getAll(anyInt(), anyInt()))
                .thenReturn(
                        generateChaosPagedResource(0, totalPages, pageSize),
                        generateChaosPagedResource(0, totalPages - 1, pageSize),
                        generateChaosPagedResource(0, totalPages - 2, pageSize),
                        generateChaosPagedResource(0, 0, pageSize)
                );

        lorisApi.setPageSize(pageSize);

        lorisApi.resetChaoses();

        verify(chaosesClient, times(totalPages + 1)).getAll(0, pageSize);
        verify(chaosesClient, times(totalPages * pageSize)).delete(any());
    }


    @Test
    public void resetApplications() throws Exception {
        final int totalPages = 3;
        final int pageSize = 1;

        ApplicationResourceHelper appHelper = ApplicationResourceHelper.builder().build();
        when(applicationClient.getAll(anyInt(), anyInt()))
                .thenReturn(
                        appHelper.generateApplicationPagedResource(0, totalPages, pageSize),
                        appHelper.generateApplicationPagedResource(0, totalPages - 1, pageSize),
                        appHelper.generateApplicationPagedResource(0, totalPages - 2, pageSize),
                        appHelper.generateApplicationPagedResource(0, 0, pageSize)
                );

        lorisApi.setPageSize(pageSize);

        lorisApi.resetApplications();

        verify(applicationClient, times(totalPages+1)).getAll(0, pageSize);
        verify(applicationClient, times(totalPages * pageSize)).delete(any());

    }


    @Test
    public void should_load_configuration_for_unique_org_space_and_app() throws Exception {
        GlobalConfiguration configuration = GlobalConfigurationBuilder.defaultTestConfig(1, 1, 1);

        Schedule lorisSchedule = new Schedule();
        lorisSchedule.setExpression(configuration.getSchedules().get("atNoon").getExpression());
        lorisSchedule.setName(configuration.getSchedules().get("atNoon").getName());
        CreateScheduleResponse scheduleResponse = new CreateScheduleResponse(lorisSchedule, new URI(Commons.BASE_URL + "/schedules/1"));
        when(scheduledClient.create(any())).thenReturn(scheduleResponse);

        List<String> appIds = new ArrayList<>();
        configuration.getOrganizations().forEach(
                (orgName, orgs) -> orgs.getSpaces().forEach(
                        (spaceName, spaces) -> spaces.getApplications().forEach(
                                (appId, application) -> appIds.add(application.getGuid())
                        )
                ));
        CreateApplicationResponse applicationResponse = CreateApplicationResponse.builder().applicationId(appIds.get(0)).location(new URI(Commons.BASE_URL + "/applications/1")).build();
        when(applicationClient.create(any())).thenReturn(applicationResponse);


        URI selfChaos = new URI(Commons.BASE_URL + "/chaoses/1");
        Chaos chaos = new Chaos();
        chaos.setProbability(0.6f);
        chaos.add(
                new Link(applicationResponse.getLocation().toString(), "application"),
                new Link(scheduleResponse.getLocation().toString(), "schedule"),
                new Link(selfChaos.toString(), "self")
        );
        CreateChaosResponse chaosResponse = CreateChaosResponse.builder().chaos(chaos).location(selfChaos).build();
        when(chaosesClient.create(any())).thenReturn(chaosResponse);

        lorisApi.loadConfiguration(configuration);
        verify(scheduledClient).create(any());
        verify(applicationClient).create(any());
        verify(chaosesClient).create(any());
    }

    @Test
    public void resetSchedules() throws Exception {
        fail("NYI");
        lorisApi.resetSchedules();
    }

    @Test
    public void toGlobaleConfiguration() throws Exception {
        fail("NYI");

    }

}