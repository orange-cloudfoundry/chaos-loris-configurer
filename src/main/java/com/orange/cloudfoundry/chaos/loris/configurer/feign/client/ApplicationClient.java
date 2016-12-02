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

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateApplicationRequest;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by O. Orand on 21/11/2016.
 */
@FeignClient(name = "LorisApplications", url = "${chaos.loris.url}")
@RequestMapping(consumes = "application/hal+json; charset=UTF-8" )
public interface ApplicationClient {
    @RequestMapping(method = RequestMethod.GET, value = "/applications",consumes = "application/hal+json; charset=UTF-8")
    List<PagedResources<com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Application>> getApplications();

    @RequestMapping(method = RequestMethod.POST, value = "/applications", consumes = "application/json")
    void create(@RequestBody CreateApplicationRequest createApplicationRequest);

    @RequestMapping(method = RequestMethod.DELETE, value = "/applications/{id}", consumes = "application/json")
    Resource delete(@PathVariable("id") String appGuid);

    @RequestMapping(method = RequestMethod.GET, value = "/applications/{id}", consumes = "application/json")
    Resource get(@PathVariable("id") String appGuid);

    @RequestMapping(value = "/",method = RequestMethod.GET )
    Resource index();

    @RequestMapping(method = RequestMethod.DELETE, value = "{url}", consumes = "application/json")
    @RequestLine("DELETE {url}")
    Resource deleteUrl(@Param("url") String url);
}

