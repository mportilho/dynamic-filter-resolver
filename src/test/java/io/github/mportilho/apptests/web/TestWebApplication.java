/*MIT License

Copyright (c) 2021 Marcelo Portilho

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

package io.github.mportilho.apptests.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.github.mportilho.apps.webapptest.WebTestingApplication;

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

	@Test
	public void givenGreetURI_whenMockMVC_thenVerifyResponse2() throws Exception {
		ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}", 2).param("name", "Bl")).andDo(print());

		MvcResult mvcResult = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.name").value("Blanka")).andExpect(jsonPath("$.foundSpec").value("true")).andReturn();
		
		assertThat(mvcResult.getResponse().getContentType()).isEqualTo(CONTENT_TYPE);
	}

}
