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

/**
 * Created by O. Orand on 07/12/2016.
 */
@Builder
public class TestResourceBuilder {
    public static final String BASE_URL = "http://chaos.loris.org";

    private int appId=1;
    private int id=1;
    private float probability=0.5f;
    private String lorisUrl=BASE_URL;
    private int scheduleId=1;


    public Chaos generateChaos() {
        Chaos chaos = new Chaos();
        chaos.add(new Link(BASE_URL+"/applications/"+appId,"application"));
        chaos.add(new Link(BASE_URL+"/schedules/"+scheduleId,"schedule"));
        chaos.add(new Link(BASE_URL+"/chaoses/"+id,"self"));
        chaos.setProbability(probability);
        return chaos;
    }

    public Chaos generateNextChaos() {
        Chaos chaos = new Chaos();
        chaos.add(new Link(BASE_URL+"/applications/"+ ++appId,"application"));
        chaos.add(new Link(BASE_URL+"/schedules/"+ ++scheduleId,"schedule"));
        chaos.add(new Link(BASE_URL+"/chaoses/"+ ++id,"self"));
        chaos.setProbability(probability);
        return chaos;
    }


}
