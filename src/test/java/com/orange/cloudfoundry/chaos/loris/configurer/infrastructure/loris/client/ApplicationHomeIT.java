package com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client;

import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Application;
import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Schedule;
import okhttp3.OkHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by O. Orand on 30/11/2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationHomeIT {

    @Autowired
    ApplicationHome applicationHome;

    @Test
    public void all_endpoints_should_availaible(){

        PagedResources<Application> appUri= applicationHome.getApplications();

        assertThat(appUri).isNotNull();

        Resources<Schedule> schedules = applicationHome.getSchedules();
        assertThat(schedules).isNotNull();



    }


    @TestConfiguration
    class ApplicationHomeTestConfig {

        @Bean
        ApplicationHome getApplicationHome(RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttp3ClientHttpRequestFactory){
            return new ApplicationHome(restTemplateBuilder,okHttp3ClientHttpRequestFactory);
        }
    }

}