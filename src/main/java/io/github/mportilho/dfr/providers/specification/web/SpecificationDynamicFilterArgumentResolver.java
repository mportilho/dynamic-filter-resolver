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

package io.github.mportilho.dfr.providers.specification.web;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.filter.DynamicFilterResolver;
import io.github.mportilho.dfr.core.statement.ConditionalStatement;
import io.github.mportilho.dfr.core.statement.ConditionalStatementProvider;
import io.github.mportilho.dfr.providers.specification.annotation.Fetching;

/**
 * Resolves interfaces assignable from {@link Specification} into valid queries.
 * These structures must be annotated with {@link Conjunction} or
 * {@link Disjunction} types.
 * 
 * @author Marcelo Portilho
 *
 */
public class SpecificationDynamicFilterArgumentResolver implements HandlerMethodArgumentResolver {

	private ConditionalStatementProvider conditionalStatementProvider;
	private DynamicFilterResolver<Specification<?>> dynamicFilterResolver;

	public SpecificationDynamicFilterArgumentResolver(ConditionalStatementProvider conditionalStatementProvider,
			DynamicFilterResolver<Specification<?>> dynamicFilterResolver) {
		this.conditionalStatementProvider = conditionalStatementProvider;
		this.dynamicFilterResolver = dynamicFilterResolver;
	}

	/**
	 * {@link Inherited}
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> parameterType = parameter.getParameterType();
		if (!parameterType.isInterface() || !Specification.class.isAssignableFrom(parameterType)) {
			return false;
		}
		int countAnnotations = countAnnotationsUntilTwo(parameter);
		if (countAnnotations > 1) {
			throw new IllegalStateException(String.format(
					"Multiples annotations of Conjunction and/or Disjunction cannot be used at the same time, as seen on parameter %s of class '%s",
					parameter.getParameterName(), parameter.getDeclaringClass()));
		}
		return countAnnotations == 1;
	}

	/**
	 * {@link Inherited}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		Map<Object, Object[]> providedParameterValuesMap = createProvidedValuesMap(parameter, webRequest);
		Map<Object, Object> contextMap = createContextMap(parameter);

		ConditionalStatement statement = conditionalStatementProvider.createConditionalStatements(
				(Class<Specification<?>>) parameter.getParameterType(), parameter.getParameterAnnotations(), providedParameterValuesMap);

		return createProxy(parameter, dynamicFilterResolver.convertTo(statement, contextMap), parameter.getParameterType());
	}

	/**
	 * Creates a new proxy object for interfaces that extends {@link Specification}
	 * 
	 * @param <T>
	 * @param parameter
	 * @param target
	 * @param targetInterface
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T createProxy(MethodParameter parameter, Object target, Class<T> targetInterface) {
		if (target == null) {
			return null;
		} else if (!Specification.class.isAssignableFrom(targetInterface)) {
			throw new IllegalStateException(String.format("The '%s' parameter must be of a interface assignable from %s",
					parameter.getParameterName(), Specification.class.getCanonicalName()));
		} else if (targetInterface.equals(target.getClass())) {
			return (T) target;
		}
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[] { targetInterface },
				(proxy, method, args) -> method.invoke(target, args));
	}

	/**
	 * Creates a context map for the {@link ConditionalStatementProvider}
	 * 
	 * @param parameter
	 * @return
	 */
	private Map<Object, Object> createContextMap(MethodParameter parameter) {
		Map<Object, Object> contextMap = new HashMap<>();
		Annotation[] anns = parameter.getParameterAnnotations();
		if (anns != null && anns.length > 0) {
			Annotation[] filteredAnnos = Stream.of(anns).filter(ann -> Fetching.class.isInstance(ann))
					.collect(collectingAndThen(toList(), l -> l.toArray(new Annotation[l.size()])));
			if (filteredAnnos != null && filteredAnnos.length > 0) {
				contextMap.put(Fetching.class, filteredAnnos);
			}
		}
		return contextMap;
	}

	/**
	 * Creates a new value provider map from the request parameters
	 * 
	 * @param parameter
	 * @param webRequest
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Object, Object[]> createProvidedValuesMap(MethodParameter parameter, NativeWebRequest webRequest) {
		Map<Object, Object[]> providedParameterValuesMap = new HashMap<>();

		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		providedParameterValuesMap.putAll(webRequest.getParameterMap());

		Map<Object, Object> pathVariables = (Map<Object, Object>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (pathVariables != null && !pathVariables.isEmpty()) {
			pathVariables.forEach((key, value) -> {
				boolean isArray = value != null ? value.getClass().isArray() : false;
				providedParameterValuesMap.put(key, isArray ? (Object[]) value : new Object[] { value });
			});
		}

		providedParameterValuesMap.putAll((Map<String, String[]>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
		return providedParameterValuesMap;
	}

	/**
	 * Counts the quantity of {@link Conjunction} and {@link Disjunction}
	 * annotations on a {@link MethodParameter}
	 * 
	 * @param parameter
	 * @return
	 */
	private int countAnnotationsUntilTwo(MethodParameter parameter) {
		int count = 0;
		for (Annotation annotation : parameter.getParameterAnnotations()) {
			if (annotation.annotationType().equals(Conjunction.class) || annotation.annotationType().equals(Disjunction.class)) {
				count++;
			}
			if (count > 1) {
				return count;
			}
		}
		return count;
	}

}
