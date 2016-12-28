package com.orange.cloudfoundry.chaos.loris.configurer.web;

import com.orange.cloudfoundry.chaos.loris.configurer.application.LorisConfigurerService;
import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfiguration;
import com.orange.cloudfoundry.chaos.loris.configurer.config.GlobalConfigurationBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author O. Orand
 */
@WebMvcTest(LorisController.class)
@RunWith(SpringRunner.class)
public class LorisControllerTest {

    @MockBean
    LorisConfigurerService lorisConfigurerServiceMock;

    @MockBean
    GlobalConfiguration globalConfiguration;

    @Autowired
    MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Test
    public void reset_endpoint_is_active() throws Exception {
        mockMvc.perform(delete("/loris/reset")).andExpect(status().is2xxSuccessful());
    }


    @Test
    public void load_endpoint_is_active() throws Exception {
        String config = json(GlobalConfigurationBuilder.defaultTestConfig(1,1,2));
        mockMvc.perform(post("/loris/load").contentType(MediaTypes.HAL_JSON).content(config)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void default_load_endpoint_is_active() throws Exception {
        mockMvc.perform(post("/loris/load/default").contentType(MediaTypes.HAL_JSON)).andExpect(status().is2xxSuccessful());
    }


    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaTypes.HAL_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

}