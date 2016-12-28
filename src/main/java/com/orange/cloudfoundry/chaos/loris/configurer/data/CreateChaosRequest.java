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
package com.orange.cloudfoundry.chaos.loris.configurer.data;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Created by O. Orand on 02/12/2016.
 */
@Builder
@Value
public class CreateChaosRequest implements CreateRequest {

    //    The URI of the application to create chaos on 	Must not be null
    @NonNull
    private String application;

    //The probability of an instance of the application experiencing chaos 	Must be at least 0,Must be at most 1,Must not be null
    @NonNull
    private Float probability;

    //The URI of the schedule to create chaos on
    @NonNull
    private String schedule;

}
