package net.apps.webapptest;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.dfr.filter.DynamicFilterResolver;
import net.dfr.operator.FilterValueConverter;
import net.dfr.provider.commons.DefaultFilterValueConverter;
import net.dfr.provider.specification.filter.SpecificationDynamicFilterResolver;
import net.dfr.provider.specification.operator.SpecificationFilterOperatorService;
import net.dfr.provider.specification.web.SpecificationFilterParameterArgumentResolver;

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
		DynamicFilterResolver<?> dynamicFilterResolver = new SpecificationDynamicFilterResolver<>(filterOperatorService, filterValueConverter);

		resolvers.add(new SpecificationFilterParameterArgumentResolver(null, dynamicFilterResolver));
	}

}
