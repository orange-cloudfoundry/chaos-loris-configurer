package com.orange.cloudfoundry.chaos.loris.configurer.domain;

import com.orange.cloudfoundry.chaos.loris.configurer.data.loris.Chaos;
import com.orange.cloudfoundry.chaos.loris.configurer.spring.template.ApplicationClient;
import com.orange.cloudfoundry.chaos.loris.configurer.spring.template.ChaosesClient;
import com.orange.cloudfoundry.chaos.loris.configurer.spring.template.ScheduledClient;
import com.orange.cloudfoundry.chaos.loris.configurer.test.TestResourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by O. Orand on 06/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class LorisApiImplTest {

    @Mock
    ChaosesClient chaosesClient;

    @Mock
    ScheduledClient scheduledClient;

    @Mock
    ApplicationClient applicationClient;

    @InjectMocks
    LorisApiImpl lorisApi;
    private String BASE_URL;


    @Test
    public void resetChaoses() throws Exception {
//        fail("NYI");
        final int pageSize=1;
        Collection<Chaos> chaosContent=new ArrayList<>();
//        chaosContent.add( new Chaos()));

        Chaos aChaos= TestResourceBuilder.builder().build().generateChaos();
        chaosContent.add(aChaos);

        PagedResources.PageMetadata chaosMetadata = new PagedResources.PageMetadata(pageSize,0,chaosContent.size());
        Link chaosLink= new Link("http://myChaos.org/1","self");
        PagedResources<Chaos> onePageChaosResources = new PagedResources<>(chaosContent,chaosMetadata,chaosLink);
        when(chaosesClient.getAll(anyInt(),anyInt())).thenReturn(onePageChaosResources);

        lorisApi.setPageSize(pageSize);

        lorisApi.resetChaoses();

        verify(chaosesClient).getAll(0,pageSize);
        verify(chaosesClient,times(chaosContent.size())).delete(any());

    }


    @Test
    public void resetApplications() throws Exception {
        fail("NYI");
        lorisApi.resetApplications();
    }

    @Test
    public void resetSchedules() throws Exception {
        fail("NYI");
        lorisApi.resetSchedules();
    }

    @Test
    public void toGlobaleConfiguration() throws Exception {
        fail("NYI");

    }

}