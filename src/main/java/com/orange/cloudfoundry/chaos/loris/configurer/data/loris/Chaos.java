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
package com.orange.cloudfoundry.chaos.loris.configurer.data.loris;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;

/**
 * Created by O. Orand on 29/11/2016.
 */
@Data
@NoArgsConstructor
public class Chaos extends ResourceSupport {

    //The probability of an instance of the application experiencing chaos 	Must be at least 0,Must be at most 1,Must not be null
    Float probability;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Chaos chaos = (Chaos) o;
        return Objects.equals(probability, chaos.probability) &&
                this.getLink("application").getHref().equals(chaos.getLink("application").getHref()) &&
                this.getLink("schedule").getHref().equals(chaos.getLink("schedule").getHref()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getLink("application").getHref(),this.getLink("schedule").getHref(),probability);
    }
}
