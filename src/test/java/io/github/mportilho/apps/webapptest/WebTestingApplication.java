package io.github.mportilho.apps.webapptest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.github.mportilho.dfr.providers.specification.autoconfiguration.EnableMvcSpecificationFilterResolver;

@EnableWebMvc
@SpringBootApplication
@EnableMvcSpecificationFilterResolver
public class WebTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebTestingApplication.class, args);
	}

}
