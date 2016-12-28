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
package com.orange.cloudfoundry.chaos.loris.configurer.test;

import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Chaos;
import lombok.Builder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by O. Orand on 07/12/2016.
 */
@Builder
public class ChaosResourceHelper {

    private int appId;
    private int id;
    private float probability;
    private String lorisUrl;
    private int scheduleId;

    public static PagedResources<Chaos> generateChaosPagedResource(int pageCount, int totalPages, int pageSize) {
        Collection<Chaos> chaosContent = new ArrayList<>();
        PagedResources.PageMetadata chaosMetadata;
        String chaosesBaseUrl = "http://myChaos.org/chaoses";
        final Link chaosLink = new Link(chaosesBaseUrl, "self");

        final Link firstLink = new Link(chaosesBaseUrl + "?page=0&size=" + pageSize, "first");
        final Link lastLink = new Link(chaosesBaseUrl + "?page=" + (totalPages - 1) + "&size=" + pageSize, "last");
        Link nextLink;
        Link prevLink;

        for (int i = 0; i < pageSize; i++) {
            Chaos aChaos = ChaosResourceHelper.builder().build().generateNextChaos();
            chaosContent.add(aChaos);
        }

        chaosMetadata = new PagedResources.PageMetadata(pageSize, pageCount, pageSize * totalPages, totalPages);
        nextLink = new Link(chaosesBaseUrl + "?page=" + (pageCount + 1) + "&size=" + pageSize, "next");
        prevLink = new Link(chaosesBaseUrl + "?page=" + (pageCount - 1) + "&size=" + pageSize, "prev");

        boolean isFirstPage = pageCount == 0;
        boolean isLastPage = pageCount == totalPages - 1;
        boolean emptyPage = totalPages == 0;
        if (emptyPage){
            Collection<Chaos> emptyContent = new ArrayList<>();
            PagedResources<Chaos> emptyResources = new PagedResources<>(emptyContent, chaosMetadata, chaosLink);
            return emptyResources;

        } else if (isFirstPage) {
            PagedResources<Chaos> firstPageChaosResources = new PagedResources<>(chaosContent, chaosMetadata, chaosLink, firstLink, lastLink, nextLink);
            return firstPageChaosResources;
        } else if (isLastPage) {
            PagedResources<Chaos> lastPageChaosResources = new PagedResources<>(chaosContent, chaosMetadata, chaosLink, firstLink, lastLink, prevLink);
            return lastPageChaosResources;

        } else {
            PagedResources<Chaos> middlePageChaosResources = new PagedResources<>(chaosContent, chaosMetadata, chaosLink, firstLink, lastLink, prevLink, nextLink);
            return middlePageChaosResources;
        }
    }


    public Chaos generateChaos() {
        Chaos chaos = new Chaos();
        chaos.add(new Link(lorisUrl+"/applications/"+appId,"application"));
        chaos.add(new Link(lorisUrl+"/schedules/"+scheduleId,"schedule"));
        chaos.add(new Link(lorisUrl+"/chaoses/"+id,"self"));
        chaos.setProbability(probability);
        return chaos;
    }

    public Chaos generateNextChaos() {
        Chaos chaos = new Chaos();
        chaos.add(new Link(lorisUrl+"/applications/"+ ++appId,"application"));
        chaos.add(new Link(lorisUrl+"/schedules/"+ ++scheduleId,"schedule"));
        chaos.add(new Link(lorisUrl+"/chaoses/"+ ++id,"self"));
        chaos.setProbability(probability);
        return chaos;
    }


    public static class ChaosResourceHelperBuilder {
        private int appId=1;
        private int id=1;
        private float probability=0.5f;
        private String lorisUrl= Commons.BASE_URL;
        private int scheduleId=1;

    }
}
