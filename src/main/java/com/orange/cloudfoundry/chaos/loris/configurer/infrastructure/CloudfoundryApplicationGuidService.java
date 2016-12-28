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
package com.orange.cloudfoundry.chaos.loris.configurer.infrastructure;

import com.orange.cloudfoundry.chaos.loris.configurer.config.Organization;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Space;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationRequest;
import com.orange.cloudfoundry.chaos.loris.configurer.data.CfApplicationResponse;
import com.orange.cloudfoundry.chaos.loris.configurer.data.cf.*;
import com.orange.cloudfoundry.chaos.loris.configurer.domain.ApplicationGuidService;
import lombok.extern.slf4j.Slf4j;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.cloudfoundry.client.v2.organizations.OrganizationResource;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v3.applications.ApplicationResource;
import org.cloudfoundry.client.v3.applications.ListApplicationsRequest;
import org.cloudfoundry.client.v3.applications.ListApplicationsResponse;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.cloudfoundry.util.PaginationUtils;
import org.cloudfoundry.util.ResourceUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author O. Orand
 */
@Slf4j
@Service
public class CloudfoundryApplicationGuidService implements ApplicationGuidService {

    private CloudFoundryClient cloudFoundryClient;
    private CloudFoundryOperations cloudFoundryOperations;


    public CloudfoundryApplicationGuidService(CloudFoundryClient cloudFoundryClient, CloudFoundryOperations cloudFoundryOperations) {
        this.cloudFoundryClient = cloudFoundryClient;
        this.cloudFoundryOperations = cloudFoundryOperations;
    }

    private static Flux<OrganizationResource> requestOrganizations(CloudFoundryClient cloudFoundryClient, Set<String> data) {
        return PaginationUtils
                .requestClientV2Resources(page -> cloudFoundryClient.organizations()
                        .list(ListOrganizationsRequest.builder()
                                .names(data)
                                .page(page)
                                .build()));
    }

    static OrganizationSummary toOrganizationSummary(OrganizationResource resource) {
        return OrganizationSummary.builder()
                .id(ResourceUtils.getId(resource))
                .name(ResourceUtils.getEntity(resource).getName())
                .build();
    }

    @Override
    public CfApplicationResponse retrieveGuid(CfApplicationRequest cfAppInfo) {
        return null;
    }

    public Map<CfApplicationRequest, CfApplicationResponse> retrieveGuid(@NotNull Map<String, Organization> configuredOrganizations) {
        Map<CfApplicationRequest, CfApplicationResponse> result = new HashMap<>();

        List<OrganizationResource> cfOrganizations = getOrganizationGuid(configuredOrganizations);

        cfOrganizations.forEach(organizationResource -> {
            OrganizationName orgName = OrganizationName.builder().value(organizationResource.getEntity().getName()).build();
            String orgGuid = organizationResource.getMetadata().getId();
            Organization organization = configuredOrganizations.get(orgName.getValue());
            if (organization != null) {
                List<SpaceResource> cfSpaces = getSpaceGuid(orgGuid, organization);
                cfSpaces.forEach(spaceResource -> {
                    SpaceName spaceName = SpaceName.builder().value(spaceResource.getEntity().getName()).build();
                    String spaceGuid = spaceResource.getMetadata().getId();

                    Space space = organization.getSpaces().get(spaceName.getValue());
                    Mono<ListApplicationsResponse> cfApplicationResponse = cloudFoundryClient.applicationsV3().list(ListApplicationsRequest.builder().names(space.getApplications().keySet()).build());
                    ListApplicationsResponse listApplicationsResponse = cfApplicationResponse.block();
                    List<ApplicationResource> cfApplications = listApplicationsResponse.getResources();
                    cfApplications.forEach(applicationResource -> {
                        ApplicationName appName = ApplicationName.builder().value(applicationResource.getName()).build();
                        ApplicationId appGuid = ApplicationId.builder().value(applicationResource.getId()).build();
                        CfApplication cfApp = CfApplication.builder().id(appGuid).name(appName).build();
                        CfApplicationRequest appRequest = CfApplicationRequest.builder().name(appName).organization(orgName).space(spaceName).build();
                        CfApplicationResponse response = CfApplicationResponse.builder().application(cfApp).organization(orgName).space(spaceName).build();
                        result.put(appRequest,response);
                    });
                });
            }else {
                log.warn("Inconsistency detected: invalid org. CF org [name: {} - guid: {} ] does not exist in Config org", orgName,orgGuid);
            }
        });
        return result;
    }

    private List<SpaceResource> getSpaceGuid(String orgGuid, Organization organization) {
        if (organization.getSpaces() == null || organization.getSpaces().size() == 0) {
            return new ArrayList<>();
        }
        ListSpacesRequest spaceRequest = ListSpacesRequest.builder().names(organization.getSpaces().keySet()).organizationId(orgGuid).build();
        Mono<ListSpacesResponse> listSpacesResponse = cloudFoundryClient.spaces().list(spaceRequest);
        return listSpacesResponse.block().getResources();
    }

    private List<OrganizationResource> getOrganizationGuid(@NotNull Map<String, Organization> organizations) {
        ListOrganizationsRequest orgRequest = ListOrganizationsRequest.builder().names(organizations.keySet()).build();
        Mono<ListOrganizationsResponse> orgPublisher;

        list(organizations.keySet());
        orgPublisher = cloudFoundryClient.organizations().list(orgRequest);
        return orgPublisher.block().getResources();
    }

    public Flux<OrganizationSummary> list(Set<String> orgsName) {
        Mono<CloudFoundryClient> cfMonoClient = Mono.just(this.cloudFoundryClient);

        return CloudfoundryApplicationGuidService.requestOrganizations(cloudFoundryClient, orgsName)
                .map(CloudfoundryApplicationGuidService::toOrganizationSummary);
    }


}
