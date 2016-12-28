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
public class SpaceBuilder {
    private static AtomicInteger spaceNameCounter = new AtomicInteger(1);

    public static Space defaultSpace(int appPerSpace) {
        Space space = new Space();
        space.setName("mySpace-" + spaceNameCounter.getAndIncrement());
        space.setGuid(UUID.randomUUID().toString());
        Application app;

        for (int i = 0; i < appPerSpace; i++) {
            app = ApplicationBuilder.defaultApp();
            space.getApplications().put(app.getName(), app);
        }
        return space;
    }


    public static Space buildWith(Application... applications) {
        Space space = new Space();
        space.setName("mySpace-" + spaceNameCounter.getAndIncrement());
        space.setGuid(UUID.randomUUID().toString());

         Arrays.asList(applications).forEach(app -> space.getApplications().put(app.getName(), app));

        return space;
    }


    public static String generateSpaceName(){
        return "app-" + spaceNameCounter.getAndIncrement();
    }


}
