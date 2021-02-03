package net.apps.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.context.NestedTestConfiguration.EnclosingConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.apps.webapptest.WebTestingApplication;

@WebMvcTest
@ContextConfiguration(classes = WebTestingApplication.class)
@WebAppConfiguration
@NestedTestConfiguration(EnclosingConfiguration.INHERIT)
public class TestWebApplication {

	private static final String CONTENT_TYPE = "application/json";

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void givenWac_whenServletContext_thenItProvidesGreetController() {
		ServletContext servletContext = wac.getServletContext();
		assertThat(servletContext).isNotNull().isInstanceOf(MockServletContext.class);
		assertThat(wac.getBean("departmentController")).isNotNull();
	}

	@Test
	public void givenGreetURI_whenMockMVC_thenVerifyResponse() throws Exception {
		final MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/department").param("name", "Bl")).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Blanka")).andReturn();
		assertThat(mvcResult.getResponse().getContentType()).isEqualTo(CONTENT_TYPE);
	}

}
