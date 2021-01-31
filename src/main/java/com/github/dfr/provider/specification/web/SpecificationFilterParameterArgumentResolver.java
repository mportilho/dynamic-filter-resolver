package com.github.dfr.provider.specification.web;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.github.dfr.annotation.And;
import com.github.dfr.annotation.Conjunction;
import com.github.dfr.annotation.Disjunction;
import com.github.dfr.annotation.Or;
import com.github.dfr.filter.ConditionalStatement;
import com.github.dfr.filter.DynamicFilterResolver;
import com.github.dfr.provider.AnnotationBasedFilterLogicContextProvider;

public class SpecificationFilterParameterArgumentResolver implements HandlerMethodArgumentResolver {

	private AnnotationBasedFilterLogicContextProvider logicContextProvider;
	private DynamicFilterResolver<Specification<?>> dynamicFilterResolver;

	public SpecificationFilterParameterArgumentResolver(StringValueResolver stringValueResolver,
			DynamicFilterResolver<Specification<?>> parameterFilter) {
		this.logicContextProvider = new AnnotationBasedFilterLogicContextProvider(stringValueResolver);
		this.dynamicFilterResolver = parameterFilter;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> parameterType = parameter.getParameterType();
		if (!parameterType.isInterface() || !Specification.class.isAssignableFrom(parameterType)) {
			return false;
		}
		int annotationQuantity = countPresentAnnotations(parameter);
		if (annotationQuantity > 1) {
			throw new IllegalStateException(
					"The annotations Or, And, Conjunction and Disjunction cannot be used at the same time, as seen on parameter + "
							+ parameter.getParameterName());
		}
		return annotationQuantity == 1;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		Map<String, String[]> parameterMap = webRequest.getParameterMap();
		parameterMap.putAll(getPathVariables(webRequest));

		ConditionalStatement statement = logicContextProvider.createConditionalStatements(parameter.getParameterAnnotation(And.class),
				parameter.getParameterAnnotation(Or.class), parameter.getParameterAnnotation(Conjunction.class),
				parameter.getParameterAnnotation(Disjunction.class), parameterMap);

		return dynamicFilterResolver.convertTo(statement);
	}

	/**
	 * 
	 * @param webRequest
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String[]> getPathVariables(NativeWebRequest webRequest) {
		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		return (Map<String, String[]>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
	}

	/**
	 * 
	 * @param parameter
	 */
	private int countPresentAnnotations(MethodParameter parameter) {
		int conjunctionFound = 0;
		int disjunctionFound = 0;
		int andFound = 0;
		int orFound = 0;

		for (Annotation annotation : parameter.getParameterAnnotations()) {
			if (annotation.annotationType().equals(Conjunction.class)) {
				conjunctionFound++;
			} else if (annotation.annotationType().equals(Disjunction.class)) {
				disjunctionFound++;
			} else if (annotation.annotationType().equals(And.class)) {
				andFound++;
			} else if (annotation.annotationType().equals(Or.class)) {
				orFound++;
			}
		}
		return conjunctionFound + disjunctionFound + andFound + orFound;
	}

}
