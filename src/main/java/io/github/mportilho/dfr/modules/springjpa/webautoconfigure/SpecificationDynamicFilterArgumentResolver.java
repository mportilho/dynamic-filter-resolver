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

import io.github.mportilho.dfr.core.processor.ConditionalStatement;
import io.github.mportilho.dfr.core.processor.ConditionalStatementProcessor;
import io.github.mportilho.dfr.core.processor.annotation.AnnotationProcessorParameter;
import io.github.mportilho.dfr.core.resolver.DynamicFilterResolver;
import io.github.mportilho.dfr.modules.spring.Fetching;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ClassUtils;
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
 * These structures must be annotated with Conjunction or Disjunction types.
 *
 * @author Marcelo Portilho
 */
public class SpecificationDynamicFilterArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String FETCHING_CLASS_NAME = Fetching.class.getCanonicalName();

    private final ConditionalStatementProcessor<AnnotationProcessorParameter> conditionalStatementProcessor;
    private final DynamicFilterResolver<Specification<?>> dynamicFilterResolver;

    public SpecificationDynamicFilterArgumentResolver(
            ConditionalStatementProcessor<AnnotationProcessorParameter> conditionalStatementProcessor,
            DynamicFilterResolver<Specification<?>> dynamicFilterResolver) {
        this.conditionalStatementProcessor = conditionalStatementProcessor;
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

        ConditionalStatement statement = conditionalStatementProcessor.createStatements(
                new AnnotationProcessorParameter(parameter.getParameterType(), parameter.getParameterAnnotations()),
                providedParameterValuesMap);
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
     * Creates a context map for the {@link ConditionalStatementProcessor}
     */
    private Map<String, Object> createContextMap(MethodParameter parameter) {
        Map<String, Object> contextMap = new HashMap<>();
        List<Annotation> list = new ArrayList<>();
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            if (annotation.annotationType().equals(io.github.mportilho.dfr.modules.spring.Fetching.class)) {
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
                getDefaultClassLoader(),
                new Class[]{targetInterface},
                (proxy, method, args) -> method.invoke(target, args));
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

}
