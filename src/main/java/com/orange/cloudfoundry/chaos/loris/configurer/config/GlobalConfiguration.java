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
package com.orange.cloudfoundry.chaos.loris.configurer.config;

import com.orange.cloudfoundry.chaos.loris.configurer.config.Organization;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Schedule;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by O. Orand on 23/11/2016.
 */
@ConfigurationProperties
@Component
@Data
public class GlobalConfiguration {
    private static final String NO_ORGANIZATION_ERROR = "Invalid configuration.No organization defined.";
    private static final String NO_SCHEDULE_ERROR = "Invalid configuration. No schedule defined";

    String url;

    @NotNull
    @Size(min = 1, message = NO_ORGANIZATION_ERROR)
    @Valid
    Map<String,Organization> organizations=new HashMap<>();


    @NotNull
    @Size(min = 1, message = NO_SCHEDULE_ERROR)
    @Valid
    Map<String, Schedule> schedules;

}
