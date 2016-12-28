package com.orange.cloudfoundry.chaos.loris.configurer.infrastructure;

import com.orange.cloudfoundry.chaos.loris.configurer.config.*;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationResponse;
import lombok.extern.slf4j.Slf4j;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.Metadata;
import org.cloudfoundry.client.v2.organizations.*;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.spaces.SpaceEntity;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.spaces.Spaces;
import org.cloudfoundry.client.v3.applications.ApplicationResource;
import org.cloudfoundry.client.v3.applications.ApplicationsV3;
import org.cloudfoundry.client.v3.applications.ListApplicationsResponse;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

/**
 * @author O. Orand
 */
@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class CloudfoundryApplicationGuidServiceTest {

    @Mock
    CloudFoundryClient cloudFoundryClient;

    @Mock
    ConnectionContext connectionContext;

    @Mock
    Organizations organizations;

    @Mock
    Spaces spaces;

    @Mock
    ApplicationsV3 applicationsV3;

    @InjectMocks
    CloudfoundryApplicationGuidService guidService;

    @Mock
    TokenProvider tokenProvider;

    @Test
    public void retrieveGuid() throws Exception {
        Application application = ApplicationBuilder.defaultApp();
        Space space = SpaceBuilder.buildWith(application);
        Organization org = OrganizationBuilder.buildWith(space);
        GlobalConfiguration orgsConfig = GlobalConfigurationBuilder.buildWith(org);


        setupOrgMock(org);

        when(cloudFoundryClient.spaces()).thenReturn(spaces);
        SpaceEntity spaceEntity = SpaceEntity.builder().name(space.getName()).build();
        Metadata spaceMetadata = Metadata.builder().id(UUID.randomUUID().toString()).url("https://mySpace").build();
        ;
        SpaceResource spaceResource = SpaceResource.builder().entity(spaceEntity).metadata(spaceMetadata).build();
        when(spaces.list(any())).thenReturn(
                Mono.just(ListSpacesResponse.builder()
                        .resource(spaceResource)
                        .totalResults(1)
                        .totalPages(1)
                        .build()
                )
        );


        when(cloudFoundryClient.applicationsV3()).thenReturn(applicationsV3);
        ApplicationResource applicationRessouce = ApplicationResource.builder().name(application.getName()).id(application.getGuid()).build();
        when(applicationsV3.list(any())).thenReturn(
                Mono.just(ListApplicationsResponse.builder()
                        .resource(applicationRessouce)
                        .build())
        );


        Map<CfApplicationRequest, CfApplicationResponse> appGuids = guidService.retrieveGuid(orgsConfig.getOrganizations());

        log.info("appGuids count: {}", appGuids.size());
        assertThat(appGuids).isNotNull();
        assertThat(appGuids.size()).isEqualTo(1);
        appGuids.forEach((name, value) -> {
            assertThat(name).isNotNull();
            assertThat(value).isNotNull();
        });

    }

    private void setupOrgMock(Organization org) {
        when(cloudFoundryClient.organizations()).thenReturn(organizations);

        OrganizationEntity orgEntity = OrganizationEntity.builder().name(org.getName()).build();
        Metadata orgMetadata = Metadata.builder().id(UUID.randomUUID().toString()).url("https://myorg").build();
        OrganizationResource orgRessource = OrganizationResource.builder().entity(orgEntity).metadata(orgMetadata).build();
        when(organizations.list(isA(ListOrganizationsRequest.class))).thenReturn(
                Mono.just(ListOrganizationsResponse.builder()
                        .resource(orgRessource)
                        .totalResults(1)
                        .totalPages(1)
                        .build()
                )
        );
    }


}