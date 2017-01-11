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
package com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author O. Orand
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LorisEndpointsIT {

    @Autowired
    LorisEndpoints lorisEndpoints;

    @Test
    public void all_endpoints_should_availaible(){

        URI appUri= lorisEndpoints.getApplicationsEndpoint();

        assertThat(appUri).isNotNull();

        URI schedulesUri = lorisEndpoints.getSchedulesEndpoint();
        assertThat(schedulesUri).isNotNull();

        URI chaosesUri = lorisEndpoints.getChaosesEndpoint();
        assertThat(chaosesUri).isNotNull();

        URI eventsUri = lorisEndpoints.getEventsEndpoint();
        assertThat(eventsUri).isNotNull();

    }



}
