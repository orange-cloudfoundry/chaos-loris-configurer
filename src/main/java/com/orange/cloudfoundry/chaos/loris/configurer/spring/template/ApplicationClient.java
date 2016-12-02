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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Optional;

/**
 * Created by O. Orand on 29/11/2016.
 */
public interface ApplicationClient {

    PagedResources<Application> getApplications(int page, int size);

    CreateApplicationResponse create(CreateApplicationRequest createApplicationRequest);

    void delete(URI location);

    Resource get(URI application);

}
