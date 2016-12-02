package com.orange.cloudfoundry.chaos.loris.configurer.spring.template;

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateChaosRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateChaosResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Chaos;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by O. Orand on 01/12/2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChaosesClientTest {

    @Autowired
    ChaosesClient chaosesClient;

    @Autowired
    ApplicationClient applicationClient;

    @Autowired
    ScheduledClient scheduledClient;

    @Test
    public void getChaoses() throws Exception {

        PagedResources<Chaos> pagedChaoses = chaosesClient.getChaoses(0,100);
        assertThat(pagedChaoses).isNotNull();

        assertThat(pagedChaoses.getContent()).isNotNull();
    }

    @Test
    public void create() throws Exception {

        Schedule schedule = createTestSchedule();


        String applicationLocation = applicationClient.create(
                CreateApplicationRequest.builder().applicationId(
                        UUID.randomUUID().toString()
                ).build())
                .getLocation().toString();


        String scheduleLocation = scheduledClient.create(schedule).getLocation().toString();
        CreateChaosRequest chaosRequest = CreateChaosRequest.builder().application(applicationLocation).schedule(scheduleLocation).probability(0.5f).build();

        chaosDoesNotExist(chaosRequest);
        CreateChaosResponse createChaosResponse = chaosesClient.create(chaosRequest);

        chaosExist(chaosRequest);


//        assertThat(createdApp.getApplicationId()).isEqualTo(appUuid);
//        assertThat(createdApp.getLocation()).isNotNull();
    }

    private Schedule createTestSchedule() {
        String name = "schedule-"+System.currentTimeMillis();
        Schedule schedule =  new Schedule();
        schedule.setName(name);
        schedule.setExpression(ScheduledClientTest.HOURLY_EXPRESSION);
        return schedule;
    }

    private void chaosDoesNotExist(CreateChaosRequest chaosRequest) {
        PagedResources<Chaos> chaoses = chaosesClient.getChaoses(0,100);
        long count= chaoses.getContent().stream()
                .filter(
                    chaos -> chaos.getLink("application").getHref().equals(chaosRequest.getApplication())&&
                            chaos.getLink("schedule").getHref().equals(chaosRequest.getSchedule())&&
                            chaos.getProbability().equals(chaosRequest.getProbability())
                ).count();
        assertThat(count).isEqualTo(0);
    }

    private void chaosExist(CreateChaosRequest chaosRequest) {
        PagedResources<Chaos> chaoses = chaosesClient.getChaoses(0,100);
        long count= chaoses.getContent().stream()
                .filter(
                        chaos -> chaos.getLink("application").getHref().equals(chaosRequest.getApplication())&&
                                chaos.getLink("schedule").getHref().equals(chaosRequest.getSchedule())&&
                                chaos.getProbability().equals(chaosRequest.getProbability())
                ).count();
        assertThat(count).isEqualTo(1);
    }

    /*
    @Test
    public void delete() throws Exception {
        Schedule schedule = createTestSchedule();
        CreateScheduleResponse createdSchedule = chaosesClient.create(schedule);

        chaosExist(schedule);

        URI application = createdSchedule.getLocation();
        chaosesClient.delete(application);

        chaosDoesNotExist(schedule);
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