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
package com.orange.cloudfoundry.chaos.loris.configurer.spring.template;

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Application;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * Created by O. Orand on 29/11/2016.
 */
@Slf4j
@Service
public class ApplicationClientImpl extends ClientImpl implements ApplicationClient {


    public ApplicationClientImpl(LorisEndpoints lorisEndpoints,RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient){
        super(lorisEndpoints,restTemplateBuilder,okHttpClient);
    }

    @Override
    public PagedResources<Application> getApplications(int page,int size) {
        ResponseEntity<PagedResources<Application>> applicationResponse = restTemplate.exchange(
                lorisEnpoints.getApplicationsEndpoint().toString()+"?page={page}&size={size}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new TypeReferences.PagedResourcesType<Application>() {},
                page,
                size);

        return applicationResponse.getBody();
    }

    @Override
    public CreateApplicationResponse create(CreateApplicationRequest createApplicationRequest) {
        URI applicationsUri = lorisEnpoints.getApplicationsEndpoint();

        URI newApplicationUri=restTemplate.postForLocation(applicationsUri,createApplicationRequest);

        return CreateApplicationResponse.builder().applicationId(createApplicationRequest.getApplicationId()).location(newApplicationUri).build();
    }

    @Override
    public void delete(URI location) {
        super.delete(location);
    }

    @Override
    public Resource get(URI application) {
        return restTemplate.getForObject(application,Resource.class);
    }

}