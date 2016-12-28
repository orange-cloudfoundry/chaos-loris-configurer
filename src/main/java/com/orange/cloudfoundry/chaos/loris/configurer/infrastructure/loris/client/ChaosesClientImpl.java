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

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateChaosRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateChaosResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Chaos;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * Created by O. Orand on 01/12/2016.
 */
@Service
@Slf4j
public class ChaosesClientImpl extends ClientImpl<Chaos> implements ChaosesClient {


    public ChaosesClientImpl(LorisEndpoints lorisEndpoints, RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient) {
        super(lorisEndpoints, restTemplateBuilder, okHttpClient);
    }

    @Override
    public PagedResources<Chaos> getAll(int page, int size) {
        ResponseEntity<PagedResources<Chaos>> chaosesResponse = restTemplate.exchange(
                lorisEnpoints.getChaosesEndpoint().toString()+"?page={page}&size={size}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new TypeReferences.PagedResourcesType<Chaos>() {},
                page,
                size);

        return chaosesResponse.getBody();    }


    @Override
    public CreateChaosResponse create(CreateChaosRequest chaosRequest) {
        URI chaosesUri = lorisEnpoints.getChaosesEndpoint();

        log.info("Creating loris chaos for {} scheduled with {}",chaosRequest.getApplication(),chaosRequest.getSchedule());
        URI newChaosUri=restTemplate.postForLocation(chaosesUri,chaosRequest);
        log.debug("Created loris chaos for {} scheduled with {}",chaosRequest.getApplication(),chaosRequest.getSchedule());

        Chaos chaos=new Chaos();
        chaos.setProbability(chaosRequest.getProbability());
        chaos.add(new Link(chaosRequest.getApplication(),"application"));
        chaos.add(new Link(chaosRequest.getSchedule(),"schedule"));
        chaos.add(new Link(newChaosUri.toString(),"self"));
        return CreateChaosResponse.builder().chaos(chaos).location(newChaosUri).build();
    }


}
