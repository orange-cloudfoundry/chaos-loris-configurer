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
package com.orange.cloudfoundry.chaos.loris.configurer.web;

//import com.orange.cloudfoundry.chaos.loris.configurer.feign.client.ApplicationClient;

import com.orange.cloudfoundry.chaos.loris.configurer.application.LorisConfigurerService;
import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by O. Orand on 23/11/2016.
 */
@RestController
@RequestMapping("/loris")
@Slf4j
public class LorisController {

    private final LorisConfigurerService lorisConfigurerService;

    private GlobalConfiguration globalConfiguration;

    public LorisController setGlobalConfiguration(GlobalConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
        return this;
    }

    @Autowired
    public LorisController(LorisConfigurerService lorisConfigurerService,GlobalConfiguration configuration){
        this.lorisConfigurerService = lorisConfigurerService;
        globalConfiguration = configuration;
    }

    @RequestMapping(path = "reset",method = RequestMethod.DELETE)
    public void resetConfiguration(){
        log.info("Resetting current Chaos Loris configuration");
        lorisConfigurerService.resetChaosLorisConfiguration();
    }

    @RequestMapping(path = "load/default", consumes = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public void loadDefaultConfiguration(){
        log.info("Loading default configuration. Summary: organizations: {} - schedules: {}",globalConfiguration.getOrganizations().size(),globalConfiguration.getSchedules().size());

        lorisConfigurerService.loadChaosLorisConfiguration(globalConfiguration);
    }

    @RequestMapping(path = "load",consumes = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public void loadConfiguration(@RequestBody GlobalConfiguration configuration){
        log.info("Loading configuration");
        lorisConfigurerService.loadChaosLorisConfiguration(configuration);
    }
}
