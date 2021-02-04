package net.apps.webapptest;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.dfr.core.converter.DefaultFilterValueConverter;
import net.dfr.core.converter.FilterValueConverter;
import net.dfr.providers.specification.filter.SpecificationDynamicFilterResolver;
import net.dfr.providers.specification.operator.SpecificationFilterOperatorService;
import net.dfr.providers.specification.web.SpecificationDynamicFilterArgumentResolver;

@EnableWebMvc
@SpringBootApplication
public class WebTestingApplication implements WebMvcRegistrations, WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(WebTestingApplication.class, args);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		SpecificationFilterOperatorService<?> filterOperatorService = new SpecificationFilterOperatorService<>();
		FilterValueConverter filterValueConverter = new DefaultFilterValueConverter();
		SpecificationDynamicFilterResolver<?> dynamicFilterResolver = new SpecificationDynamicFilterResolver<>(filterOperatorService,
				filterValueConverter);

		resolvers.add(new SpecificationDynamicFilterArgumentResolver(null, dynamicFilterResolver));
	}

}
