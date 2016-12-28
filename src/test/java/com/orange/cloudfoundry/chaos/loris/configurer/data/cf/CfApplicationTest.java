package com.orange.cloudfoundry.chaos.loris.configurer.data.cf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author O. Orand
 */
public class CfApplicationTest {

    @Test(expected = NullPointerException.class)
    public void application_id_should_not_be_null() {

        try {
            CfApplication.builder()
                    .name(ApplicationName.builder().value("myApp").build())
                    .build();
        } catch (NullPointerException npe) {
            assertThat(npe.getMessage()).isEqualTo("id");
            throw npe;
        }
    }

    @Test(expected = NullPointerException.class)
    public void application_name_should_not_be_null() {

        try {
            CfApplication.builder()
                    .id(ApplicationId.builder().value("1234-4567-4567-4568").build())
                    .build();
        } catch (NullPointerException npe) {
            assertThat(npe.getMessage()).isEqualTo("name");
            throw npe;
        }
    }


    @Test
    public void should_create_an_application_with_a_name_a_space_and_an_organisation() {
        ApplicationId myId=ApplicationId.builder().value("1234-1234-1234-1234").build();
        ApplicationName myName=ApplicationName.builder().value("myApp").build();

        CfApplication myApp = CfApplication.builder()
                .id(myId)
                .name(myName)
                .build();
        assertThat(myApp).isNotNull();
        assertThat(myApp.getId()).isEqualTo(myId);
        assertThat(myApp.getName()).isEqualTo(myName);
    }

}