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
package com.orange.cloudfoundry.chaos.loris.configurer.feign.client;

import com.orange.cloudfoundry.chaos.loris.configurer.feign.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Application;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by O. Orand on 21/11/2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ApplicationClientTest {

//    @Rule
//    public WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicHttpsPort());

    @Autowired
    private ApplicationClient client;


    @Test
    public void test(){
//        givenThat(get(urlEqualTo("/applications")).withRequestBody(matchingJsonPath("$.applicationId"))
//                .willReturn(aResponse()
//                        .withStatus(201)
//                        .withBody("{Location: https://chaos-lemur/applications/50}")));
        Resource index = client.index();
        String applicationUrl= index.getLink("applications").toString();
        List<PagedResources<Application>> appList = client.getApplications();
  //      assertThat(appList.get(0).getContent().isEmpty()).isTrue();


//        CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest("9ee9d2ba-5ef8-4ef6-a29b-212ab38a017c");
 //       client.create(createApplicationRequest);

        appList = client.getApplications();
//        assertThat(appList.get(0).getContent().size()).isEqualTo(1);
//        appList.get(0).getContent().forEach(x-> assertThat(x.getApplicationId()).isEqualTo("9ee9d2ba-5ef8-4ef6-a29b-212ab38a017c"));

        Optional<Application> app = appList.get(0).getContent().stream().filter(appGuid -> appGuid.getApplicationId().equals("9ee9d2ba-5ef8-4ef6-a29b-212ab38a017c")).findFirst();
        app.ifPresent(application -> client.deleteUrl(application.getLink("self").getHref()));


    }

//    @Configuration
//    class Config{
//
//        @Bean
//        ApplicationClient getApplicationClient(){
//            int httpsPort = wireMock.httpsPort();
//            log.info("Wiremock port: {}",httpsPort);
//            return Feign.builder().target(ApplicationClient.class,"https://localhost:"+httpsPort);
//        }
//    }

}
