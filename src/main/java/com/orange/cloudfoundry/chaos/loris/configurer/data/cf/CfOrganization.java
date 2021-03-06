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
package com.orange.cloudfoundry.chaos.loris.configurer.data.cf;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

/**
 * @author O. Orand
 */
@Builder
@Value
public class CfOrganization {
    private OrganizationId id;
    private OrganizationName name;
    private Map<SpaceName,CfSpace> applications;
}
