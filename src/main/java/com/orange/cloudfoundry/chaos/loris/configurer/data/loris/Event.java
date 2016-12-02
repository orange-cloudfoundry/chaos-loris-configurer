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
package com.orange.cloudfoundry.chaos.loris.configurer.data.loris;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Created by O. Orand on 29/11/2016.
 */
@Data
@NoArgsConstructor
public class Event extends ResourceSupport {

    //    The instances terminated during the event
    String executedAt;

    //    The total number of instances that were candidates for termination during the event
    int totalInstanceCount;

    //    An ISO-8601 timestamp for when the event occurred
    List<Integer> terminatedInstances;

    //    Number 	The total number of instances terminated during the event
    int terminatedInstanceCount;
}
