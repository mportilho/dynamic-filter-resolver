package io.github.mportilho.dfr.providers.specification.web;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
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

public class SpecificationDynamicFilterArgumentResolver implements HandlerMethodArgumentResolver {

	private ConditionalStatementProvider conditionalStatementProvider;
	private DynamicFilterResolver<Specification<?>> dynamicFilterResolver;

	public SpecificationDynamicFilterArgumentResolver(ConditionalStatementProvider conditionalStatementProvider,
			DynamicFilterResolver<Specification<?>> dynamicFilterResolver) {
		this.conditionalStatementProvider = conditionalStatementProvider;
		this.dynamicFilterResolver = dynamicFilterResolver;
	}

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

	@Override
	@SuppressWarnings("unchecked")
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		Map<Object, Object[]> providedParameterValuesMap = createProvidedValuesMap(parameter, webRequest);
		Map<Object, Object> contextMap = createContextMap(parameter);

		ConditionalStatement statement = conditionalStatementProvider.createConditionalStatements(
				(Class<Specification<?>>) parameter.getParameterType(), parameter.getParameterAnnotations(), providedParameterValuesMap);

		return createProxy(dynamicFilterResolver.convertTo(statement, contextMap), parameter.getParameterType());
	}

	@SuppressWarnings("unchecked")
	private <T> T createProxy(Object target, Class<T> targetInterface) {
		if (target == null) {
			return null;
		} else if (targetInterface.equals(target.getClass())) {
			return (T) target;
		}
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[] { targetInterface },
				(proxy, method, args) -> method.invoke(target, args));
	}

	/**
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
	 * 
	 * @param parameter
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
