package com.orange.cloudfoundry.chaos.loris.configurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableFeignClients
@Slf4j
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ChaosLorisConfigurerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaosLorisConfigurerApplication.class, args);
    }

    @Bean
    public Decoder feignDecoder() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .registerModule(new Jackson2HalModule());

        return new ResponseEntityDecoder(new JacksonDecoder(mapper));
    }


    @Bean
    public LinkDiscoverer linkDiscoverer() {
        return new HalLinkDiscoverer();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient) {
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        return restTemplateBuilder.requestFactory(okHttp3ClientHttpRequestFactory).build();
    }

}
