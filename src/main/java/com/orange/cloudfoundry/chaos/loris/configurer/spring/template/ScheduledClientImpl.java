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

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateScheduleResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Application;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by O. Orand on 01/12/2016.
 */
@Slf4j
@Service
public class ScheduledClientImpl implements ScheduledClient {

    private final LorisEndpoints lorisEnpoints;
    RestTemplate restTemplate;

    public ScheduledClientImpl(LorisEndpoints lorisEndpoints, RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient){
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory= new OkHttp3ClientHttpRequestFactory(okHttpClient);
        this.restTemplate=restTemplateBuilder.requestFactory(okHttp3ClientHttpRequestFactory).build();
        this.lorisEnpoints = lorisEndpoints;
    }

    @Override
    public PagedResources<Schedule> getSchedules(int page, int size) {
        ResponseEntity<PagedResources<Schedule>> scheduleResponse = this.restTemplate.exchange(
                lorisEnpoints.getSchedulesEndpoint().toString()+"?page={page}&size={size}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new TypeReferences.PagedResourcesType<Schedule>() {},
                page,
                size);

        return scheduleResponse.getBody();    }

    @Override
    public CreateScheduleResponse create(Schedule schedule) {

        URI applicationsUri = lorisEnpoints.getSchedulesEndpoint();

        URI newApplicationUri=restTemplate.postForLocation(applicationsUri,schedule);

        return new CreateScheduleResponse(schedule,newApplicationUri);
    }

    @Override
    public void delete(URI location) {
        restTemplate.delete(location);

    }
}
