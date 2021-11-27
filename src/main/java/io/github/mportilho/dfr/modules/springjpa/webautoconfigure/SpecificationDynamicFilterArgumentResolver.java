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

import br.com.bancoamazonia.base.components.dynafilter.annotation.Conjunction;
import br.com.bancoamazonia.base.components.dynafilter.annotation.Disjunction;
import br.com.bancoamazonia.base.components.dynafilter.filter.DynamicFilterResolver;
import br.com.bancoamazonia.base.components.dynafilter.processor.reflection.ReflectionConditionalStatementProcessor;
import br.com.bancoamazonia.base.components.dynafilter.statement.ConditionalStatement;
import br.com.bancoamazonia.base.components.resource.ResourceLoader;
import br.com.bancoamazonia.sbs.components.dynafilter.springdatajpa.annotation.Fetching;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves interfaces assignable from {@link Specification} into valid queries.
 * These structures must be annotated with {@link Conjunction} or
 * {@link Disjunction} types.
 *
 * @author Marcelo Portilho
 */
public class SpecificationDynamicFilterArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String FETCHING_CLASS_NAME = Fetching.class.getCanonicalName();

    private final ReflectionConditionalStatementProcessor reflectionConditionalStatementProcessor;
    private final DynamicFilterResolver<Specification<?>> dynamicFilterResolver;

    public SpecificationDynamicFilterArgumentResolver(ReflectionConditionalStatementProcessor reflectionConditionalStatementProcessor,
                                                      DynamicFilterResolver<Specification<?>> dynamicFilterResolver) {
        this.reflectionConditionalStatementProcessor = reflectionConditionalStatementProcessor;
        this.dynamicFilterResolver = dynamicFilterResolver;
    }

    /**
     * {@link Inherited}
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return parameterType.isInterface() && Specification.class.isAssignableFrom(parameterType);
    }

    /**
     * {@link Inherited}
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Map<String, Object[]> providedParameterValuesMap = createProvidedValuesMap(webRequest);
        Map<String, Object> contextMap = createContextMap(parameter);

        ConditionalStatement statement = reflectionConditionalStatementProcessor.createConditionalStatements(
                parameter.getParameterType(), parameter.getParameterAnnotations(), providedParameterValuesMap);
        return createProxy(dynamicFilterResolver.convertTo(statement, contextMap), parameter.getParameterType());
    }

    /**
     * Creates a new value provider map from the request parameters
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object[]> createProvidedValuesMap(NativeWebRequest webRequest) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, Object[]> providedParameterValuesMap = new HashMap<>(webRequest.getParameterMap());
        if (httpServletRequest == null) {
            throw new IllegalStateException("Obligatory HTTP Context not found");
        }

        Map<String, Object> pathVariables = (Map<String, Object>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null && !pathVariables.isEmpty()) {
            for (Map.Entry<String, Object> entry : pathVariables.entrySet()) {
                boolean isArray = entry.getValue() != null && entry.getValue().getClass().isArray();
                providedParameterValuesMap.put(entry.getKey(), isArray ? (String[]) entry.getValue() : new String[]{(String) entry.getValue()});
            }
        }
        return providedParameterValuesMap;
    }

    /**
     * Creates a context map for the {@link ReflectionConditionalStatementProcessor}
     */
    private Map<String, Object> createContextMap(MethodParameter parameter) {
        Map<String, Object> contextMap = new HashMap<>();
        List<Annotation> list = new ArrayList<>();
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            if (annotation.annotationType().equals(Fetching.class)) {
                list.add(annotation);
            }
        }

        Annotation[] filteredAnnotations = list.toArray(new Annotation[0]);
        if (filteredAnnotations.length > 0) {
            contextMap.put(FETCHING_CLASS_NAME, filteredAnnotations);
        }
        return contextMap;
    }

    /**
     * Creates a new proxy object for interfaces that extends {@link Specification}
     */
    @SuppressWarnings("unchecked")
    private <T> T createProxy(Object target, Class<T> targetInterface) {
        if (target == null) {
            return null;
        } else if (targetInterface.equals(target.getClass())) {
            return (T) target;
        }
        return (T) Proxy.newProxyInstance(
                ResourceLoader.getClassLoader(),
                new Class[]{targetInterface},
                (proxy, method, args) -> method.invoke(target, args));
    }

}
