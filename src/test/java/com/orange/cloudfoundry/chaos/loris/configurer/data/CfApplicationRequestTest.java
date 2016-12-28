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

import com.orange.cloudfoundry.chaos.loris.configurer.data.cf.ApplicationName;
import com.orange.cloudfoundry.chaos.loris.configurer.data.cf.OrganizationName;
import com.orange.cloudfoundry.chaos.loris.configurer.data.cf.SpaceName;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by O. Orand on 21/11/2016.
 */
@Slf4j
public class CfApplicationRequestTest {

    private final SpaceName spaceName = SpaceName.builder().value("mySpace").build();
    private final OrganizationName organizationName = OrganizationName.builder().value("myOrg").build();
    private final ApplicationName myApplication=ApplicationName.builder().value("myApp").build();

    @Test(expected = NullPointerException.class)
    public void application_name_should_not_be_null() {

        try {
            CfApplicationRequest.builder()
                    .organization(organizationName)
                    .space(spaceName)
                    .build();
        } catch (NullPointerException npe) {
            assertThat(npe.getMessage()).isEqualTo("name");
            throw npe;
        }
    }

    @Test(expected = NullPointerException.class)
    public void organization_name_should_not_be_null() {

        try {
            CfApplicationRequest.builder()
                    .name(myApplication)
                    .space(spaceName)
                    .build();
        } catch (NullPointerException npe) {
            assertThat(npe.getMessage()).isEqualTo("organization");
            throw npe;
        }
    }

    @Test(expected = NullPointerException.class)
    public void space_name_should_not_be_null() {

        try {
            CfApplicationRequest.builder()
                    .organization(organizationName)
                    .name(myApplication)
                    .build();
        } catch (NullPointerException npe) {
            assertThat(npe.getMessage()).isEqualTo("space");
            throw npe;
        }
    }


    @Test
    public void should_create_an_application_request() {

        CfApplicationRequest app = CfApplicationRequest.builder()
                .name(
                        myApplication
                )
                .space(spaceName)
                .organization(organizationName)
                .build();
        assertThat(app).isNotNull();
        assertThat(app.getName()).isEqualTo(myApplication);
        assertThat(app.getOrganization()).isEqualTo(organizationName);
        assertThat(app.getSpace()).isEqualTo(spaceName);
    }



}
