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

import com.orange.cloudfoundry.chaos.loris.configurer.config.Application;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by O. Orand on 23/11/2016.
 */
@Data
public class Space {

    private static final String NO_APPLICATION_ERROR = "Invalid configuration. No application defined";

    @NotNull
    String guid;

    String name;

    @Setter(AccessLevel.PACKAGE)
    @NotNull
    @Size(min = 1, message = NO_APPLICATION_ERROR)
    @Valid
    Map<String, Application> applications = new HashMap<>();

}
