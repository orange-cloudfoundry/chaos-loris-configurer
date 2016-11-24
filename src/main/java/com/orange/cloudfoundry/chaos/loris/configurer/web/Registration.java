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

import com.orange.cloudfoundry.chaos.loris.configurer.client.ApplicationClient;
import com.orange.cloudfoundry.chaos.loris.configurer.config.ChaosLoris;
import com.orange.cloudfoundry.chaos.loris.configurer.data.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by O. Orand on 23/11/2016.
 */
@RestController
@RequestMapping("/")
@Slf4j
public class Registration {
    ApplicationClient applicationClient;

    @Autowired
    public Registration(ApplicationClient applicationClient, ChaosLoris chaosLoris){
        this.applicationClient=applicationClient;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void registerApplications(){
        log.info("Existing apps:");
        List<Application> applications = applicationClient.getApplications();
        for (Application app :
                applications) {
            log.info("\t- {}",app);
        }
    }
}