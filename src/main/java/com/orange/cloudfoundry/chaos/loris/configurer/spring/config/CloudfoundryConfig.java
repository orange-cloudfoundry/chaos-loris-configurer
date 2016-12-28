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
package com.orange.cloudfoundry.chaos.loris.configurer.spring.config;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.ProxyConfiguration;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.UaaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * @author O. Orand
 */
@Configuration
public class CloudfoundryConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean(name="cfProxy")
    public Optional<ProxyConfiguration> proxyConfiguration(
            @Value("${cf.proxy.enable:false}") Optional<Boolean> enable,
            @Value("${cf.proxy.host:}") Optional<String> proxyHost,
            @Value("${cf.proxy.port:}") Optional<Integer> proxyPort

    ) {
        if (!enable.orElse(false) || ! proxyHost.isPresent() || proxyHost.get().length()==0){
            return Optional.empty();
        }

        return Optional.of(ProxyConfiguration.builder().host(proxyHost.get()).port(proxyPort.orElse(3128)).build());

    }


    @Bean(name="cfProxyBean")
    public ProxyConfiguration proxyConfigurationBean(
            @Value("${cf.proxy.enable:false}") Optional<Boolean> enable,
            @Value("${cf.proxy.host:}") Optional<String> proxyHost,
            @Value("${cf.proxy.port:}") Optional<Integer> proxyPort

    ) {
        if (!enable.orElse(false) || ! proxyHost.isPresent() || proxyHost.get().length()==0){
            return null;
        }

        return ProxyConfiguration.builder().host(proxyHost.get()).port(proxyPort.orElse(3128)).build();

    }
    @Bean
    DefaultConnectionContext connectionContext(
            @Value("${cf.apiHost}") String apiHost,
            @Value("${cf.trustSelfSignedCertificates:false}") boolean trustSelfSignedCertificates,
            @Qualifier(value = "cfProxyBean") ProxyConfiguration proxyConfiguration) {
        return DefaultConnectionContext.builder()
                .apiHost(apiHost)
                .skipSslValidation(trustSelfSignedCertificates)
//                .proxyConfiguration(applicationContext.getBean("cfProxy",Optional.class))
                .proxyConfiguration(Optional.ofNullable(proxyConfiguration))
                .build();
    }

    @Bean
    PasswordGrantTokenProvider tokenProvider(@Value("${cf.username}") String username,
                                             @Value("${cf.password}") String password) {
        return PasswordGrantTokenProvider.builder()
                .password(password)
                .username(username)
                .build();
    }

    @Bean
    ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Bean
    ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorDopplerClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Bean
    ReactorUaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorUaaClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }



    @Bean
    CloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
                                                  DopplerClient dopplerClient,
                                                  UaaClient uaaClient,
                                                  @Value("${cf.organization:}") String organization,
                                                  @Value("${cf.space:}") String space) {
        return DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .dopplerClient(dopplerClient)
                .uaaClient(uaaClient)
//                    .organization(organization)
//                    .space(space)
                .build();
    }
}
