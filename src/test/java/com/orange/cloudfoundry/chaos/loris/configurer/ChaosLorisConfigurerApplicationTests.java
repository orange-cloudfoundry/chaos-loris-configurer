package com.orange.cloudfoundry.chaos.loris.configurer;

import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ChaosLorisConfigurerApplicationTests {

	@Autowired
	GlobalConfiguration conf;

	@Test
	public void contextLoads() {
		log.info("Configuration summary: ");
		log.info(" Organizations declared: {}", conf.getOrganizations().size());
		log.info(" Schedules declared: {}", conf.getSchedules().size());
		assertThat(conf.getOrganizations()).hasSize(1);
		assertThat(conf.getSchedules()).hasSize(3);
	}

}
