package com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client;

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateScheduleRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateScheduleResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by O. Orand on 01/12/2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ScheduledClientTest {
    final public static String HOURLY_EXPRESSION = "0 0 * * * *";

    @Autowired
    ScheduledClient scheduledClient;

    @Test
    public void getSchedules() throws Exception {

        PagedResources<Schedule> pagedSchedules = scheduledClient.getAll(0,100);
        assertThat(pagedSchedules).isNotNull();

        assertThat(pagedSchedules.getContent()).isNotNull();
    }

    @Test
    public void create() throws Exception {

        Schedule schedule = createTestSchedule();

        scheduleDoesNotExist(schedule);

        CreateScheduleRequest scheduleRequest=CreateScheduleRequest.builder().name(schedule.getName()).expression(schedule.getExpression()).build();
        CreateScheduleResponse createdSchedule = scheduledClient.create(scheduleRequest);

        scheduleExist(schedule);


//        assertThat(createdApp.getApplicationId()).isEqualTo(appUuid);
//        assertThat(createdApp.getLocation()).isNotNull();
    }

    private Schedule createTestSchedule() {
        String name = "schedule-"+System.currentTimeMillis();
        Schedule schedule =  new Schedule();
        schedule.setName(name);
        schedule.setExpression(HOURLY_EXPRESSION);
        return schedule;
    }

    private void scheduleDoesNotExist(Schedule aSchedule) {
        PagedResources<Schedule> schedules = scheduledClient.getAll(0,100);
        long count= schedules.getContent().stream().filter(schedule -> schedule.getName().equals(aSchedule.getName())&&schedule.getExpression().equals(aSchedule.getExpression())).count();
        assertThat(count).isEqualTo(0);
    }

    private void scheduleExist(Schedule aSchedule) {
        PagedResources<Schedule> schedules = scheduledClient.getAll(0,100);
        long count= schedules.getContent().stream().filter(schedule -> schedule.getName().equals(aSchedule.getName())&&schedule.getExpression().equals(aSchedule.getExpression())).count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void delete() throws Exception {
        Schedule schedule = createTestSchedule();
        CreateScheduleRequest scheduleRequest=CreateScheduleRequest.builder().name(schedule.getName()).expression(schedule.getExpression()).build();
        CreateScheduleResponse createdSchedule = scheduledClient.create(scheduleRequest);

        scheduleExist(schedule);

        URI application = createdSchedule.getLocation();
        scheduledClient.delete(application);

        scheduleDoesNotExist(schedule);
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