package net.dfr.spring.autoconfigure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringValueResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.dfr.core.converter.DefaultFilterValueConverter;
import net.dfr.core.converter.FilterValueConverter;
import net.dfr.core.filter.DynamicFilterResolver;
import net.dfr.core.operator.FilterOperatorService;
import net.dfr.core.statement.ConditionalStatementProvider;
import net.dfr.core.statement.DefaultConditionalStatementProvider;
import net.dfr.core.statement.ValueExpressionResolver;
import net.dfr.providers.specification.filter.SpecificationDynamicFilterResolver;
import net.dfr.providers.specification.operator.SpecificationFilterOperatorService;
import net.dfr.providers.specification.web.SpecificationDynamicFilterArgumentResolver;

@ConditionalOnClass({ EnableJpaRepositories.class, EnableWebMvc.class })
public class MvcDynamicFilterResolverAutoConfiguration {

	@Autowired(required = false)
	private StringValueResolver stringValueResolver;

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
				FilterOperatorService<Specification<?>> filterOperatorService = new SpecificationFilterOperatorService();
				FilterValueConverter filterValueConverter = new DefaultFilterValueConverter();
				DynamicFilterResolver<Specification<?>> dynamicFilterResolver = new SpecificationDynamicFilterResolver(filterOperatorService,
						filterValueConverter);

				ValueExpressionResolver resolver = stringValueResolver != null ? (value -> stringValueResolver.resolveStringValue(value)) : null;
				ConditionalStatementProvider statementProvider = new DefaultConditionalStatementProvider(resolver);
				resolvers.add(new SpecificationDynamicFilterArgumentResolver(statementProvider, dynamicFilterResolver));
			}
		};
	}

}
