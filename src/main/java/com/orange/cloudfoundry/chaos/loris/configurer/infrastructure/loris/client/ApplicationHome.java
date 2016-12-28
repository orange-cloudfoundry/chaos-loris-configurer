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

import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Application;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;

/**
 * Created by O. Orand on 30/11/2016.
 */
@Service
public class ApplicationHome {


    @Value("${chaos.loris.url}")
    URI baseUrl;
    private Traverson traverson;
    private RestTemplate restTemplate;

    public ApplicationHome(RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient){
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory= new OkHttp3ClientHttpRequestFactory(okHttpClient);
        this.restTemplate=restTemplateBuilder.requestFactory(okHttp3ClientHttpRequestFactory).build();
    }


    @PostConstruct
    private void init(){
//        this.restTemplate = new RestTemplateBuilder().build();

        MediaType halMediaType = new MediaType(HAL_JSON, Charset.forName("UTF-8"));
//        restTemplate.setMessageConverters(Traverson.getDefaultMessageConverters(halMediaType));
        this.traverson= new Traverson(baseUrl, halMediaType).setRestOperations(restTemplate);
    }

    public PagedResources<Application> getApplications() {
        ParameterizedTypeReference<List<PagedResources<Application>>> typeReference= new ParameterizedTypeReference<List<PagedResources<Application>>>(){};

//        List<PagedResources<Application>> applications2= this.traverson.follow("$._links.applications.href").toObject(typeReference);

        PagedResources<Application> applications= this.traverson.follow("$._links.applications.href").toObject(new TypeReferences.PagedResourcesType<Application>());
        return applications;
    }



    public Resources<Schedule> getSchedules() {
        ParameterizedTypeReference<Resources<Schedule>> resourceParameterizedTypeReference =
                new ParameterizedTypeReference<Resources<Schedule>>() {};
        Resources<Schedule> schedules= this.traverson.follow("$._links.schedules.href").toObject(resourceParameterizedTypeReference);
        return schedules;
    }


    public enum rootEndpoint{ }
}
