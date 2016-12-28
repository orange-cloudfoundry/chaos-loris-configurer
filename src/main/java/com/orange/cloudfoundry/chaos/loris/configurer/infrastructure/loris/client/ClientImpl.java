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

import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by O. Orand on 01/12/2016.
 */
public abstract class ClientImpl<LorisType> {


    protected final RestTemplate restTemplate;
    protected final LorisEndpoints lorisEnpoints;

    public ClientImpl(LorisEndpoints lorisEndpoints, RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient) {
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        this.restTemplate = restTemplateBuilder.requestFactory(okHttp3ClientHttpRequestFactory).build();
        this.lorisEnpoints = lorisEndpoints;
    }


    public void delete(URI location) {
        restTemplate.delete(location);
    }

    public Resource get(URI application) {
        return restTemplate.getForObject(application,Resource.class);
    }

    public PagedResources<LorisType> getPagedResourcesAt(URI location) {
            ResponseEntity<PagedResources<LorisType>> applicationResponse = restTemplate.exchange(
                    location,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new TypeReferences.PagedResourcesType<LorisType>() {}
            );

            return applicationResponse.getBody();
        }
    }
