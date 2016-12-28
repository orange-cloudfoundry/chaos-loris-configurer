package com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client;

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by O. Orand on 01/12/2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationClientTest {

    @Autowired
    ApplicationClient applicationClient;

    @Test
    public void getApplications() throws Exception {

        PagedResources<Application> pagedApplications = applicationClient.getAll(0,100);
        assertThat(pagedApplications).isNotNull();

        assertThat(pagedApplications.getContent()).isNotNull();
    }

    @Test
    public void create() throws Exception {
        String appUuid = UUID.randomUUID().toString();

        applicationDoesNotExist(appUuid);

        CreateApplicationResponse createdApp = applicationClient.create(CreateApplicationRequest.builder().applicationId(appUuid).build());

        applicationExist(appUuid);


        assertThat(createdApp.getApplicationId()).isEqualTo(appUuid);
        assertThat(createdApp.getLocation()).isNotNull();
    }

    private void applicationDoesNotExist(String appUuid) {
        PagedResources<Application> pagedApplicationsBeforeCreation = applicationClient.getAll(0,100);
        long appCountBeforeCreation= pagedApplicationsBeforeCreation.getContent().stream().filter(application -> application.getApplicationId().equals(appUuid)).count();
        assertThat(appCountBeforeCreation).isEqualTo(0);
    }

    private void applicationExist(String appUuid) {
        PagedResources<Application> pagedApplicationsAfterCreation = applicationClient.getAll(0,100);
        long appCountAfterCreation= pagedApplicationsAfterCreation.getContent().stream().filter(application -> application.getApplicationId().equals(appUuid)).count();
        assertThat(appCountAfterCreation).isEqualTo(1);
    }

    @Test
    public void delete() throws Exception {
        String appUuid = UUID.randomUUID().toString();
        CreateApplicationResponse createdApp = applicationClient.create(CreateApplicationRequest.builder().applicationId(appUuid).build());

        applicationExist(appUuid);

        URI application = createdApp.getLocation();
        applicationClient.delete(application);

        applicationDoesNotExist(appUuid);
    }

    @Test
    @Ignore
    public void get() throws Exception {

        String appUuid = UUID.randomUUID().toString();
        CreateApplicationResponse createdApp = applicationClient.create(CreateApplicationRequest.builder().applicationId(appUuid).build());

        URI applicationUri = createdApp.getLocation();
        Resource application= applicationClient.get(applicationUri);




    }

}