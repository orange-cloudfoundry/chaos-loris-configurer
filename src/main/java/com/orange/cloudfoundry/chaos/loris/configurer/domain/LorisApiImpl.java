/**
 * Copyright (C) 2016 Orange
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orange.cloudfoundry.chaos.loris.configurer.domain;

import com.orange.cloudfoundry.chaos.loris.configurer.config.Application;
import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Organization;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Schedule;
import com.orange.cloudfoundry.chaos.loris.configurer.data.*;
import com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client.ApplicationClient;
import com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client.ChaosesClient;
import com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client.Client;
import com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client.ScheduledClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by O. Orand on 05/12/2016.
 */
@Service
@Slf4j
public class LorisApiImpl implements LorisApi {

    public static final int FIRST_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 100;

    private final ApplicationClient applicationClient;
    private final ScheduledClient scheduledClient;
    private final ChaosesClient chaosesClient;
    private int pageSize;

    public LorisApiImpl(ApplicationClient applicationClient, ScheduledClient scheduledClient, ChaosesClient chaosesClient) {
        this.applicationClient = applicationClient;
        this.scheduledClient = scheduledClient;
        this.chaosesClient = chaosesClient;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public int getPageSize() {
        return pageSize;
    }

    public LorisApiImpl setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    private void delete(Client client) {
        boolean endProcessing = false;
        for (; !endProcessing; ) {
            PagedResources<? extends ResourceSupport> firstChaosResourcePage = client.getAll(FIRST_PAGE, this.pageSize);

            firstChaosResourcePage.forEach(lorisResource -> {
                try {
                    log.info("Deleting resource {}",lorisResource.getLink("self").getHref());
                    client.delete(new URI(lorisResource.getLink("self").getHref()));
                } catch (URISyntaxException e) {
                    new RuntimeException(e);
                }
            });
            boolean hasProcessedAllPages = firstChaosResourcePage.getMetadata().getTotalPages() == 0;
            if (hasProcessedAllPages) {
                endProcessing = true;
            }
        }
    }

    @Override
    public void resetChaoses() {
        delete(chaosesClient);
    }

    @Override
    public void resetApplications() {
        delete(applicationClient);
    }

    @Override
    public void resetSchedules() {
        delete(scheduledClient);
    }

    @Override
    public GlobalConfiguration toGlobaleConfiguration() {
        return null;
    }

    @Override
    public void loadConfiguration(GlobalConfiguration configuration) {

        Map<String, URI> scheduleLocation = processToScheduleCreation(configuration.getSchedules());
        Map<String, URI> applicationLocation = processToApplicationCreation(configuration.getOrganizations(), scheduleLocation);


    }

    private Map<String, URI> processToApplicationCreation(Map<String, Organization> organizations, Map<String, URI> scheduleLocation) {
        HashMap<String, URI> createdApplication = new HashMap<>();
        organizations.forEach(
                (orgName, orgs) -> orgs.getSpaces().forEach(
                        (spaceName, spaces) -> spaces.getApplications().forEach(
                                (appName, application) -> {
                                    createApplication(createdApplication, application)
                                            .ifPresent( appResponse ->
                                                    createChaos(application, scheduleLocation, appResponse, orgName, spaceName, appName)
                                            );
                                }
                        )
                )
        );

        return createdApplication;

    }

    private void createChaos(Application application, Map<String, URI> scheduleLocation, CreateApplicationResponse applicationResponse, String orgName, String spaceName, String applicationName) {
        URI scheduleUri = scheduleLocation.get(application.getScheduleName());
        if (scheduleUri == null) {
            log.warn("Invalid schedule name {} for app {} in space {} in org {}", application.getScheduleName(), applicationName, spaceName, orgName);
        } else {
            CreateChaosResponse chaosResponse = chaosesClient.create(
                    CreateChaosRequest.builder()
                            .probability(application.getChaosProbability())
                            .application(applicationResponse.getLocation().toString())
                            .schedule(scheduleUri.toString())
                            .build()
            );
        }
    }

    private Optional<CreateApplicationResponse> createApplication(HashMap<String, URI> result, Application application) {
        CreateApplicationResponse appResponse=null;
        if (application.getGuid() != null && application.getGuid().length()>0) {
            appResponse = applicationClient.create(CreateApplicationRequest.builder().applicationId(application.getGuid()).build());
            result.put(application.getGuid(), appResponse.getLocation());
        }
        return Optional.ofNullable(appResponse);
    }

    private Map<String, URI> processToScheduleCreation(Map<String, Schedule> schedules) {
        HashMap<String, URI> result = new HashMap<>();
        schedules.forEach(
                (schedName, schedule) -> {
                    CreateScheduleResponse response = scheduledClient.create(CreateScheduleRequest.builder().name(schedName).expression(schedule.getExpression()).build());
                    result.put(schedName, response.getLocation());
                }
        );

        return result;
    }
}
