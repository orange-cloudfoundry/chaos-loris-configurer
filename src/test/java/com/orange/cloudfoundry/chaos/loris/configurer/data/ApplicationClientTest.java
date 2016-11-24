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
package com.orange.cloudfoundry.chaos.loris.configurer.data;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.niws.client.http.RestClient;
import com.orange.cloudfoundry.chaos.loris.configurer.client.ApplicationClient;
import feign.Feign;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Created by O. Orand on 21/11/2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationClientTest {

    @Rule
    public WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicHttpsPort());

    @Autowired
    private ApplicationClient client;


    @Test
    public void test(){
        givenThat(get(urlEqualTo("/applications")).withRequestBody(matchingJsonPath("$.applicationId"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody("{Location: https://chaos-lemur/applications/50}")));

        client.getApplications();
    }



    @Configuration
    class Config{

        @Bean
        ApplicationClient getApplicationClient(){
            int httpsPort = wireMock.httpsPort();

            return Feign.builder().target(ApplicationClient.class,"https://localhost:"+httpsPort);
        }

    }

}
