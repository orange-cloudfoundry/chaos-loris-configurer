package com.orange.cloudfoundry.chaos.loris.configurer.infrastructure;

import com.orange.cloudfoundry.chaos.loris.configurer.ChaosLorisConfigurerApplication;
import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Organization;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.spring.config.CloudfoundryConfig;
import lombok.extern.slf4j.Slf4j;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author O. Orand
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChaosLorisConfigurerApplication.class, CloudfoundryConfig.class, CloudfoundryApplicationGuidServiceIT.Config.class, GlobalConfiguration.class})
@Slf4j
public class CloudfoundryApplicationGuidServiceIT {

    @Autowired
    CloudFoundryOperations cloudFoundryOperations;

    @Autowired
    CloudfoundryApplicationGuidService applicationGuidService;

    @Autowired
    GlobalConfiguration globalConfiguration;

    @Test
    public void retrieveGuid() throws Exception {

        Map<String, Organization> organizations = new HashMap<>();
        Flux<OrganizationSummary> organizationSummaryFlux = cloudFoundryOperations.organizations().list();
        List<String> orgNames = new ArrayList<>();
        System.out.println(" Step 1 - cccc xxxxx");
        organizationSummaryFlux
                .map(organizationSummary -> {
                    Organization org = new Organization();
                    org.setGuid(organizationSummary.getId());
                    org.setSpaces(new HashMap<>());
                    return Tuples.of(organizationSummary.getName(), org);
                }).map(orgNameOrgTuple -> {
                    organizations.put(orgNameOrgTuple.getT1(), orgNameOrgTuple.getT2());
                    System.out.println("Step1b map size: " + organizations.size());
                    return orgNameOrgTuple;
                }).blockLast();

//        organizationSummaryFlux.blockLast();
        System.out.println("Step2 - cccc xxxxx xxxxxxxxxxxxx - size: " + orgNames.size() + "- map size: " + organizations.size());
//        cloudFoundryOperations.organizations().list().map(OrganizationSummary::toString).subscribe(organizationSummary -> {
//            System.out.print("n°2-");
//            System.out.println(organizationSummary);
//        });
//        System.out.println("Step 3 - cccc xxxxx xxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyy");
//        cloudFoundryOperations.organizations().list().doOnNext(organizationSummary -> {
//            System.out.print("n°3-");
//            System.out.println(organizationSummary);
//        }).then().block();


//        applicationGuidService.retrieveGuid(Collections.unmodifiableMap(organizations));
//

        Map<CfApplicationRequest, CfApplicationResponse> x = applicationGuidService.retrieveGuid(globalConfiguration.getOrganizations());
        log.info( "retrieveGuid: {}",x.size());

    }


    @Configuration
    public static class Config {

        @Bean
        CloudfoundryApplicationGuidService cloudfoundryApplicationGuidService(CloudFoundryClient cloudFoundryClient, CloudFoundryOperations cloudFoundryOperations) {
            return new CloudfoundryApplicationGuidService(cloudFoundryClient, cloudFoundryOperations);
        }
    }
}