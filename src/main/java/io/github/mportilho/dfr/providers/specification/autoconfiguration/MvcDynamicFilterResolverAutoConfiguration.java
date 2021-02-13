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
import io.github.mportilho.dfr.core.statement.ValueExpressionResolver;
import io.github.mportilho.dfr.core.statement.annontation.AnnotationConditionalStatementProvider;
import io.github.mportilho.dfr.core.statement.annontation.DefaultAnnotationConditionalStatementProvider;
import io.github.mportilho.dfr.modules.spring.conversionservice.SpringConversionServiceAdapter;
import io.github.mportilho.dfr.providers.specification.filter.SpecificationDynamicFilterResolver;
import io.github.mportilho.dfr.providers.specification.operator.SpecificationFilterOperatorService;
import io.github.mportilho.dfr.providers.specification.web.SpecificationDynamicFilterArgumentResolver;

/**
 * Applies autoconfiguration of
 * {@link SpecificationDynamicFilterArgumentResolver} for a Spring MVC context
 * 
 * @author Marcelo Portilho
 *
 */
@ConditionalOnClass({ EnableJpaRepositories.class, EnableWebMvc.class })
public class MvcDynamicFilterResolverAutoConfiguration {

	private StringValueResolver stringValueResolver;
	private ConversionService conversionService;

	public MvcDynamicFilterResolverAutoConfiguration(@Autowired(required = false) StringValueResolver stringValueResolver,
			@Autowired(required = false) ConversionService conversionService) {
		this.stringValueResolver = stringValueResolver;
		this.conversionService = conversionService;
	}

	/**
	 * @return A bean of {@link WebMvcConfigurer} for adding the
	 *         {@link SpecificationDynamicFilterArgumentResolver} to the Spring MVC
	 *         configuration
	 */
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

				AnnotationConditionalStatementProvider statementProvider = new DefaultAnnotationConditionalStatementProvider(resolver);
				resolvers.add(new SpecificationDynamicFilterArgumentResolver(statementProvider, dynamicFilterResolver));
			}
		};
	}

}
