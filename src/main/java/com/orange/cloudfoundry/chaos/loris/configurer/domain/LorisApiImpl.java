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

import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Chaos;
import com.orange.cloudfoundry.chaos.loris.configurer.spring.template.ApplicationClient;
import com.orange.cloudfoundry.chaos.loris.configurer.spring.template.ChaosesClient;
import com.orange.cloudfoundry.chaos.loris.configurer.spring.template.ScheduledClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by O. Orand on 05/12/2016.
 */
@Service
public class LorisApiImpl implements LorisApi {

    public static final int FIRST_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 100;

    private final ApplicationClient applicationClient;
    private final ScheduledClient scheduledClient;
    private final ChaosesClient chaosesClient;

    public int getPageSize() {
        return pageSize;
    }

    public LorisApiImpl setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    private int pageSize;

    public LorisApiImpl(ApplicationClient applicationClient, ScheduledClient scheduledClient, ChaosesClient chaosesClient) {
        this.applicationClient = applicationClient;
        this.scheduledClient = scheduledClient;
        this.chaosesClient = chaosesClient;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    @Override
    public void resetChaoses() {
        boolean endProcessing  = false;
        for (; !endProcessing; ) {
            PagedResources<Chaos> pagedResources = chaosesClient.getAll(FIRST_PAGE, this.pageSize);

            pagedResources.forEach(chaos -> {
                try {
                    chaosesClient.delete(new URI(chaos.getLink("self").getHref()));
                } catch (URISyntaxException e) {
                    new RuntimeException(e);
                }
            });
            if (pagedResources.getMetadata().getTotalPages() == 1) {
                endProcessing = true;
            }
        }
    }

    @Override
    public void resetApplications() {

    }

    @Override
    public void resetSchedules() {

    }

    @Override
    public GlobalConfiguration toGlobaleConfiguration() {
        return null;
    }
}
