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
package com.orange.cloudfoundry.chaos.loris.configurer.application;

import com.orange.cloudfoundry.chaos.loris.configurer.config.Application;
import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Organization;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Space;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.domain.ApplicationGuidService;
import com.orange.cloudfoundry.chaos.loris.configurer.domain.LorisApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author O. Orand
 */
@Service
@Slf4j
public class LorisConfigurerService {


    private final LorisApi lorisApi;
    private final ApplicationGuidService applicationGuidService;

    public LorisConfigurerService(LorisApi lorisApi, ApplicationGuidService applicationGuidService){
        this.lorisApi = lorisApi;
        this.applicationGuidService = applicationGuidService;
    }


    public void resetChaosLorisConfiguration(){
        lorisApi.resetChaoses();
        lorisApi.resetApplications();
        lorisApi.resetSchedules();
    }


    public void loadChaosLorisConfiguration(GlobalConfiguration globalConfiguration){
        Map<CfApplicationRequest, CfApplicationResponse> applicationGuids = applicationGuidService.retrieveGuid(globalConfiguration.getOrganizations());
        Map<String, Organization> updatedConfig = updateConfigutationWithAppGuid(globalConfiguration.getOrganizations(), applicationGuids);
        globalConfiguration.setOrganizations(updatedConfig);
        lorisApi.loadConfiguration(globalConfiguration);
    }

    private Map<String, Organization> updateConfigutationWithAppGuid(Map<String, Organization> organizations, Map<CfApplicationRequest, CfApplicationResponse> applicationGuids) {
        Map<String, Organization> result= organizations;

        applicationGuids.values().forEach( cfApplicationResponse -> {
            Organization currentOrg = organizations.get(cfApplicationResponse.getOrganization().getValue());
            Space currentSpace = currentOrg.getSpaces().get(cfApplicationResponse.getSpace().getValue());
            Application currentApplication = currentSpace.getApplications().get(cfApplicationResponse.getApplication().getName().getValue());
            currentApplication.setGuid(cfApplicationResponse.getApplication().getId().getValue());

        });

        return result;
    }


    public GlobalConfiguration getChaosLorisConfiguration() {
        return lorisApi.toGlobaleConfiguration();
    }
}
