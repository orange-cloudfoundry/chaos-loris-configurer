package com.orange.cloudfoundry.chaos.loris.configurer;

import com.orange.cloudfoundry.chaos.loris.configurer.data.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class ChaosLorisConfigurerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaosLorisConfigurerApplication.class, args);
    }


}
