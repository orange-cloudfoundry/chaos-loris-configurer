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

import com.orange.cloudfoundry.chaos.loris.configurer.data.CreateScheduleResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import org.springframework.hateoas.PagedResources;

import java.net.URI;

/**
 * Created by O. Orand on 01/12/2016.
 */
public interface ScheduledClient {

    PagedResources<Schedule> getSchedules(int page, int size);

    CreateScheduleResponse create(Schedule schedule);

    void delete(URI location);
}
