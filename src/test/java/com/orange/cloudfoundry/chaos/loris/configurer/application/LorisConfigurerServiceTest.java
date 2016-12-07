package com.orange.cloudfoundry.chaos.loris.configurer.application;

import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Organization;
import com.orange.cloudfoundry.chaos.loris.configurer.config.Schedule;
import com.orange.cloudfoundry.chaos.loris.configurer.domain.LorisApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author O. Orand
 */
@RunWith(MockitoJUnitRunner.class)
public class LorisConfigurerServiceTest {

    @InjectMocks
    LorisConfigurerService lorisConfigurerService;

    @Mock
    LorisApi lorisApi;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void resetChaosLorisConfiguration() throws Exception {

        lorisConfigurerService.resetChaosLorisConfiguration();

        verify(lorisApi).resetChaoses();
        verify(lorisApi).resetApplications();
        verify(lorisApi).resetSchedules();
    }

    @Test
    public void loadChaosLorisConfiguration() throws Exception {
        fail("NYI");
        GlobalConfiguration myConfig=null;
        lorisConfigurerService.loadChaosLorisConfiguration(myConfig);
    }

}