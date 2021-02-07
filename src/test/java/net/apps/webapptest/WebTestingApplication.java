package net.apps.webapptest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import net.dfr.providers.specification.autoconfiguration.EnableMvcSpecificationFilterResolver;

@EnableWebMvc
@SpringBootApplication
@EnableMvcSpecificationFilterResolver
public class WebTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebTestingApplication.class, args);
	}

}
