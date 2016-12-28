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

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateScheduleRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateScheduleResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * Created by O. Orand on 01/12/2016.
 */
@Slf4j
@Service
public class ScheduledClientImpl extends ClientImpl<Schedule> implements ScheduledClient  {


    public ScheduledClientImpl(LorisEndpoints lorisEndpoints, RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient) {
        super(lorisEndpoints, restTemplateBuilder, okHttpClient);
    }

    public PagedResources<Schedule> getAll(int page, int size) {
        ResponseEntity<PagedResources<Schedule>> scheduleResponse = this.restTemplate.exchange(
                lorisEnpoints.getSchedulesEndpoint().toString()+"?page={page}&size={size}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new TypeReferences.PagedResourcesType<Schedule>() {},
                page,
                size);

        return scheduleResponse.getBody();    }

    @Override
    public CreateScheduleResponse create(CreateScheduleRequest scheduleRequest) {
        log.info("Creating schedule <{}>",scheduleRequest.getName());
        URI schedulesLocation = lorisEnpoints.getSchedulesEndpoint();

        URI newApplicationUri=restTemplate.postForLocation(schedulesLocation,scheduleRequest);
        log.debug("Created schedule <{}>",scheduleRequest.getName());

        Schedule schedule = new Schedule();
        schedule.setExpression(scheduleRequest.getExpression());
        schedule.setName(scheduleRequest.getName());
        return new CreateScheduleResponse(schedule,newApplicationUri);
    }


    @Override
    public Resource get(URI schedule) {
        return null;
    }
}
