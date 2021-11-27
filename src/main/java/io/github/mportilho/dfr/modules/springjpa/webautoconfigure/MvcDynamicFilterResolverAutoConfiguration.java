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

package io.github.mportilho.dfr.modules.springjpa.webautoconfigure;

import br.com.bancoamazonia.base.components.dynafilter.filter.DefaultFilterValueConverter;
import br.com.bancoamazonia.base.components.dynafilter.filter.DynamicFilterResolver;
import br.com.bancoamazonia.base.components.dynafilter.filter.FilterValueConverter;
import br.com.bancoamazonia.base.components.dynafilter.processor.reflection.ReflectionConditionalStatementProcessor;
import br.com.bancoamazonia.base.components.dynafilter.processor.reflection.ReflectionConditionalStatementProcessorImpl;
import br.com.bancoamazonia.base.components.dynafilter.statement.ValueExpressionResolver;
import br.com.bancoamazonia.sbs.components.converter.SpringFormattedConversionServiceAdapter;
import br.com.bancoamazonia.sbs.components.dynafilter.springdatajpa.filter.SpecificationDynamicFilterResolver;
import br.com.bancoamazonia.sbs.components.dynafilter.springdatajpa.operator.SpecificationFilterOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringValueResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Applies autoconfiguration of
 * {@link SpecificationDynamicFilterArgumentResolver} for a Spring MVC context
 *
 * @author Marcelo Portilho
 */
@ConditionalOnClass({EnableJpaRepositories.class, EnableWebMvc.class})
public class MvcDynamicFilterResolverAutoConfiguration implements EmbeddedValueResolverAware {

    private StringValueResolver stringValueResolver;
    private final ConversionService conversionService;

    public MvcDynamicFilterResolverAutoConfiguration(@Autowired(required = false) ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * @return A bean of {@link WebMvcConfigurer} for adding the
     * {@link SpecificationDynamicFilterArgumentResolver} to the Spring MVC
     * configuration
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                ValueExpressionResolver resolver = stringValueResolver != null ? (stringValueResolver::resolveStringValue) : null;

                FilterValueConverter filterValueConverter;
                if (conversionService != null) {
                    filterValueConverter = new DefaultFilterValueConverter(new SpringFormattedConversionServiceAdapter(conversionService));
                } else {
                    filterValueConverter = new DefaultFilterValueConverter();
                }

                DynamicFilterResolver<Specification<?>> dynamicFilterResolver = new SpecificationDynamicFilterResolver(
                        new SpecificationFilterOperatorService(),
                        filterValueConverter);

                ReflectionConditionalStatementProcessor statementProvider = new ReflectionConditionalStatementProcessorImpl(resolver);
                resolvers.add(new SpecificationDynamicFilterArgumentResolver(statementProvider, dynamicFilterResolver));
            }
        };
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
    }
}
