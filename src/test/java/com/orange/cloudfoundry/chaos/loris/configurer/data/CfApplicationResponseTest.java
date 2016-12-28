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

import com.orange.cloudfoundry.chaos.loris.configurer.data.cf.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by O. Orand on 21/11/2016.
 */
@Slf4j
public class CfApplicationResponseTest {

    private final SpaceName spaceName = SpaceName.builder().value("mySpace").build();
    private final OrganizationName organizationName = OrganizationName.builder().value("myOrg").build();
    private CfApplication myApplication=CfApplication.builder()
            .id(ApplicationId.builder().value("1234-1234-1234-1234").build())
            .name(ApplicationName.builder().value("myApp").build())
            .build();

    @Test(expected = NullPointerException.class)
    public void application_should_not_be_null() {

        try {
            CfApplicationResponse cfApplicationResponse = CfApplicationResponse.builder()
                    .organization(organizationName)
                    .space(spaceName)
                    .build();
        } catch (NullPointerException npe) {
            assertThat(npe.getMessage()).isEqualTo("application");
            throw npe;
        }
    }

    @Test(expected = NullPointerException.class)
    public void organization_name_should_not_be_null() {

        try {
            CfApplicationResponse cfApplicationResponse = CfApplicationResponse.builder()
                    .application(myApplication)
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
            CfApplicationResponse cfApplicationResponse = CfApplicationResponse.builder()
                    .organization(organizationName)
                    .application(myApplication)
                    .build();
        } catch (NullPointerException npe) {
            assertThat(npe.getMessage()).isEqualTo("space");
            throw npe;
        }
    }



    @Test
    public void should_create_an_application_response() {

        CfApplicationResponse app = CfApplicationResponse.builder()
                .application(
                        myApplication
                )
                .space(spaceName)
                .organization(organizationName)
                .build();
        assertThat(app).isNotNull();
        assertThat(app.getApplication()).isEqualTo(myApplication);
        assertThat(app.getOrganization()).isEqualTo(organizationName);
        assertThat(app.getSpace()).isEqualTo(spaceName);
    }

}
