package com.orange.cloudfoundry.chaos.loris.configurer;

import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.infrastructure.loris.client.LorisEndpoints;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ChaosLorisConfigurerApplicationIT {

	@Mock
	LorisEndpoints lorisEndpoints;

	@Autowired
	GlobalConfiguration conf;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void contextLoads() {
		when(lorisEndpoints.getApplicationsEndpoint()).thenThrow(new RuntimeException("toto"));

		log.info("Configuration summary: ");
		log.info(" Organizations declared: {}", conf.getOrganizations().size());
		log.info(" Schedules declared: {}", conf.getSchedules().size());
		assertThat(conf.getOrganizations()).hasSize(1);
		assertThat(conf.getSchedules()).hasSize(3);
	}

	@Configuration
	public final static class Config{

		@Bean
		LorisEndpoints lorisEndpoints(){
			return null;
		}

	}
}
