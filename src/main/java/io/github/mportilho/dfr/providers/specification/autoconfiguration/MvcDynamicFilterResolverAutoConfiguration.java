package io.github.mportilho.dfr.providers.specification.autoconfiguration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringValueResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.mportilho.dfr.core.converter.DefaultFilterValueConverter;
import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.DynamicFilterResolver;
import io.github.mportilho.dfr.core.operator.FilterOperatorService;
import io.github.mportilho.dfr.core.statement.ConditionalStatementProvider;
import io.github.mportilho.dfr.core.statement.DefaultConditionalStatementProvider;
import io.github.mportilho.dfr.core.statement.ValueExpressionResolver;
import io.github.mportilho.dfr.modules.spring.conversionservice.SpringConversionServiceAdapter;
import io.github.mportilho.dfr.providers.specification.filter.SpecificationDynamicFilterResolver;
import io.github.mportilho.dfr.providers.specification.operator.SpecificationFilterOperatorService;
import io.github.mportilho.dfr.providers.specification.web.SpecificationDynamicFilterArgumentResolver;

@ConditionalOnClass({ EnableJpaRepositories.class, EnableWebMvc.class })
public class MvcDynamicFilterResolverAutoConfiguration {

	private StringValueResolver stringValueResolver;
	private ConversionService conversionService;

	public MvcDynamicFilterResolverAutoConfiguration(@Autowired(required = false) StringValueResolver stringValueResolver,
			@Autowired(required = false) ConversionService conversionService) {
		this.stringValueResolver = stringValueResolver;
		this.conversionService = conversionService;
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
				FilterOperatorService<Specification<?>> filterOperatorService = new SpecificationFilterOperatorService();
				ValueExpressionResolver resolver = stringValueResolver != null ? (value -> stringValueResolver.resolveStringValue(value)) : null;

				FilterValueConverter filterValueConverter;
				if (conversionService != null) {
					filterValueConverter = new DefaultFilterValueConverter(new SpringConversionServiceAdapter(conversionService));
				} else {
					filterValueConverter = new DefaultFilterValueConverter();
				}

				DynamicFilterResolver<Specification<?>> dynamicFilterResolver = new SpecificationDynamicFilterResolver(filterOperatorService,
						filterValueConverter);

				ConditionalStatementProvider statementProvider = new DefaultConditionalStatementProvider(resolver);
				resolvers.add(new SpecificationDynamicFilterArgumentResolver(statementProvider, dynamicFilterResolver));
			}
		};
	}

}
