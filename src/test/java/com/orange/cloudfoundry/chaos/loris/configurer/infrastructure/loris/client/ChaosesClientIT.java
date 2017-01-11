package com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client;

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateChaosRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateChaosResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateScheduleRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Chaos;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by O. Orand on 01/12/2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChaosesClientIT {

    @Autowired
    ChaosesClient chaosesClient;

    @Autowired
    ApplicationClient applicationClient;

    @Autowired
    ScheduledClient scheduledClient;

    @Test
    public void getChaoses() throws Exception {

        PagedResources<Chaos> pagedChaoses = chaosesClient.getAll(0,100);
        assertThat(pagedChaoses).isNotNull();

        assertThat(pagedChaoses.getContent()).isNotNull();
    }

    @Test
    public void create() throws Exception {

        CreateChaosRequest chaosRequest = getCreateChaosRequest();

        chaosDoesNotExist(chaosRequest);
        CreateChaosResponse createChaosResponse = chaosesClient.create(chaosRequest);

        chaosExist(chaosRequest);


//        assertThat(createdApp.getApplicationId()).isEqualTo(appUuid);
//        assertThat(createdApp.getLocation()).isNotNull();
    }

    private CreateChaosRequest getCreateChaosRequest() {
        Schedule schedule = createTestSchedule();


        String applicationLocation = applicationClient.create(
                CreateApplicationRequest.builder().applicationId(
                        UUID.randomUUID().toString()
                ).build())
                .getLocation().toString();


        CreateScheduleRequest scheduleRequest=CreateScheduleRequest.builder().name(schedule.getName()).expression(schedule.getExpression()).build();
        String scheduleLocation = scheduledClient.create(scheduleRequest).getLocation().toString();
        return CreateChaosRequest.builder().application(applicationLocation).schedule(scheduleLocation).probability(0.5f).build();
    }

    private Schedule createTestSchedule() {
        String name = "schedule-"+System.currentTimeMillis();
        Schedule schedule =  new Schedule();
        schedule.setName(name);
        schedule.setExpression(ScheduledClientIT.HOURLY_EXPRESSION);
        return schedule;
    }

    private void chaosDoesNotExist(CreateChaosRequest chaosRequest) {
        PagedResources<Chaos> chaoses = chaosesClient.getAll(0,100);
        long count= chaoses.getContent().stream()
                .filter(
                    chaos -> chaos.getLink("application").getHref().equals(chaosRequest.getApplication())&&
                            chaos.getLink("schedule").getHref().equals(chaosRequest.getSchedule())&&
                            chaos.getProbability().equals(chaosRequest.getProbability())
                ).count();
        assertThat(count).isEqualTo(0);
    }

    private void chaosExist(CreateChaosRequest chaosRequest) {
        PagedResources<Chaos> chaoses = chaosesClient.getAll(0,100);
        long count= chaoses.getContent().stream()
                .filter(
                        chaos -> chaos.getLink("application").getHref().equals(chaosRequest.getApplication())&&
                                chaos.getLink("schedule").getHref().equals(chaosRequest.getSchedule())&&
                                chaos.getProbability().equals(chaosRequest.getProbability())
                ).count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void delete() throws Exception {

        CreateChaosRequest chaosRequest=getCreateChaosRequest();
        CreateChaosResponse createdChaos = chaosesClient.create(chaosRequest);

        chaosExist(chaosRequest);

        URI application = createdChaos.getLocation();
        chaosesClient.delete(application);

        chaosDoesNotExist(chaosRequest);
    }
/*
    @Test
    public void get() throws Exception {

        String appUuid = UUID.randomUUID().toString();
        CreateApplicationResponse createdApp = applicationClient.create(CreateApplicationRequest.builder().applicationId(appUuid).build());

        URI applicationUri = createdApp.getLocation();
        Resource application= applicationClient.get(applicationUri);
    }
*/
}