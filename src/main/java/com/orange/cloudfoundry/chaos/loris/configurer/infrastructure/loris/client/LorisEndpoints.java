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

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by O. Orand on 01/12/2016.
 */
//@Configuration
@Service
@Slf4j
public class LorisEndpoints {

    private LinkDiscoverer linkDiscoverer;
    private String indexPage;

    @Value("${chaos.loris.url}")
    URI baseUrl;

    @Value("${chaos.loris.applications.rel}")
    private String applicationsRelName;

    @Value("${chaos.loris.chaoses.rel}")
    private String chaosesRelName;

    @Value("${chaos.loris.events.rel}")
    private String eventsRelName;

    @Value("${chaos.loris.schedules.rel}")
    private String schedulesRelName;


    private final RestTemplate restTemplate;

    public LorisEndpoints(RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient, LinkDiscoverer linkDiscoverer){
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory= new OkHttp3ClientHttpRequestFactory(okHttpClient);
        this.restTemplate=restTemplateBuilder.requestFactory(okHttp3ClientHttpRequestFactory).build();
        this.linkDiscoverer = linkDiscoverer;
    }

    @PostConstruct
    public void init(){
        indexPage = restTemplate.getForObject(baseUrl.toString(), String.class);
    }


    public URI getApplicationsEndpoint() {
        return extractHref(applicationsRelName);
    }

    private URI extractHref(String relName) {
        try {
            return new URI(linkDiscoverer.findLinkWithRel(relName,indexPage).getHref());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public URI getSchedulesEndpoint() {
        return extractHref(schedulesRelName);
    }

    public URI getChaosesEndpoint() {
        return extractHref(chaosesRelName);
    }

    public URI getEventsEndpoint() {
        return extractHref(eventsRelName);
    }
}
