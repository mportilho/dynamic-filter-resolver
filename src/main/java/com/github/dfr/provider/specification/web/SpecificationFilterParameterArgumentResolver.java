package com.github.dfr.provider.specification.web;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.github.dfr.annotation.Conjunction;
import com.github.dfr.annotation.Disjunction;
import com.github.dfr.filter.ConditionalStatement;
import com.github.dfr.filter.DynamicFilterResolver;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.provider.SpecificationConditionalStatementProvider;
import com.github.dfr.provider.specification.annotation.Fetch;
import com.github.dfr.provider.specification.annotation.Fetches;

public class SpecificationFilterParameterArgumentResolver implements HandlerMethodArgumentResolver {

	private SpecificationConditionalStatementProvider conditionalStatementProvider;
	private DynamicFilterResolver<Specification<?>> dynamicFilterResolver;

	public SpecificationFilterParameterArgumentResolver(StringValueResolver stringValueResolver,
			DynamicFilterResolver<Specification<?>> parameterFilter) {
		this.conditionalStatementProvider = new SpecificationConditionalStatementProvider(stringValueResolver);
		this.dynamicFilterResolver = parameterFilter;
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
		Map<String, String[]> providedParameterValuesMap = new HashMap<>();

		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		providedParameterValuesMap.putAll(webRequest.getParameterMap());
		providedParameterValuesMap.putAll((Map<String, String[]>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));

		Function<FilterParameter, FilterParameter> decorator = createParameterDecorator(parameter);

		ConditionalStatement statement = conditionalStatementProvider.createConditionalStatements(parameter.getParameterType(),
				parameter.getMethodAnnotations(), providedParameterValuesMap);
		return (statement != null && statement.hasAnyCondition()) ? dynamicFilterResolver.convertTo(statement) : Specification.where(null);
	}

	/**
	 * 
	 * @param parameter
	 * @return
	 */
	private Function<FilterParameter, FilterParameter> createParameterDecorator(MethodParameter parameter) {
		Function<FilterParameter, FilterParameter> decorator = null;
		Fetches fetches = parameter.getMethodAnnotation(Fetches.class);
		Map<String, Fetch> fetchingMap = new HashMap<>();
		if (fetches != null && fetches.value() != null && fetches.value().length > 0) {
			for (Fetch fetch : fetches.value()) {
				for (String path : fetch.value()) {
					fetchingMap.put(path, fetch);
				}
			}
			decorator = param -> {
				param.addState(Fetches.class, fetchingMap);
				return param;
			};
		}
		return decorator;
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
