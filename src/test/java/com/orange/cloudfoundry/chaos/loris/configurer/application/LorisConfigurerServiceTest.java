package com.orange.cloudfoundry.chaos.loris.configurer.application;

import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfigurationBuilder;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Organization;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.cf.*;
import com.orange.cloudfoundry.chaos.loris.configurer.domain.ApplicationGuidService;
import com.orange.cloudfoundry.chaos.loris.configurer.domain.LorisApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;


/**
 * @author O. Orand
 */
@RunWith(MockitoJUnitRunner.class)
public class LorisConfigurerServiceTest {

    @InjectMocks
    LorisConfigurerService lorisConfigurerService;

    @Mock
    LorisApi lorisApi;

    @Mock
    ApplicationGuidService applicationGuidService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void resetChaosLorisConfiguration() throws Exception {

        lorisConfigurerService.resetChaosLorisConfiguration();

        verifyZeroInteractions(applicationGuidService);
        verify(lorisApi).resetChaoses();
        verify(lorisApi).resetApplications();
        verify(lorisApi).resetSchedules();
    }

    @Test
    public void loadChaosLorisConfiguration() throws Exception {
        GlobalConfiguration myConfig = GlobalConfigurationBuilder.defaultTestConfig(1, 2, 2);
        Map<CfApplicationRequest, CfApplicationResponse> cfResponse = createResponseFrom(myConfig.getOrganizations());
        when(applicationGuidService.retrieveGuid(myConfig.getOrganizations())).thenReturn(cfResponse);

        lorisConfigurerService.loadChaosLorisConfiguration(myConfig);

        verify(lorisApi).loadConfiguration(isA(GlobalConfiguration.class));
        verify(applicationGuidService).retrieveGuid(myConfig.getOrganizations());
    }

    private Map<CfApplicationRequest, CfApplicationResponse> createResponseFrom(Map<String, Organization> myConfig) {
        Map<CfApplicationRequest, CfApplicationResponse> result = new HashMap<>();
        myConfig.forEach((orgName, organization) -> {
            organization.getSpaces().forEach((spaceName, space) -> {
                space.getApplications().forEach( (appName,app) -> {
                    CfApplication cfApplication = CfApplication.builder()
                            .name(ApplicationName.builder().value(appName).build())
                            .id(ApplicationId.builder().value(UUID.randomUUID().toString()).build())
                            .build();
                    CfApplicationRequest request = CfApplicationRequest.builder().name(cfApplication.getName())
                            .organization(OrganizationName.builder().value(orgName).build())
                            .space(SpaceName.builder().value(spaceName).build())
                            .build();

                    CfApplicationResponse response = CfApplicationResponse.builder().application(cfApplication)
                            .organization(OrganizationName.builder().value(orgName).build())
                            .space(SpaceName.builder().value(spaceName).build())
                            .build();
                    result.put(request,response);
                });
            });
        });
        return result;
    }

}