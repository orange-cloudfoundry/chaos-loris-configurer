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
package com.orange.cloudfoundry.chaos.loris.configurer.config;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author O. Orand
 */
public class OrganizationBuilder {
    private static AtomicInteger orgNameCounter = new AtomicInteger(1);

    public static Organization defaultOrg(int spacePerOrg, int appPerSpace) {
        Organization org = new Organization();
        org.setName("myOrg-" + orgNameCounter.getAndIncrement());
        org.setGuid(UUID.randomUUID().toString());
        Space space;

        for (int i = 0; i < spacePerOrg; i++) {
            space = SpaceBuilder.defaultSpace(appPerSpace);
            org.getSpaces().put(space.getName(), space);
        }
        return org;
    }

    public static Organization buildWith(Space... spaces) {
        Organization org = new Organization();
        org.setName("myOrg-" + orgNameCounter.getAndIncrement());
        org.setGuid(UUID.randomUUID().toString());

        Arrays.asList(spaces).forEach(space -> org.getSpaces().put(space.getName(), space));

        return org;
    }


}
