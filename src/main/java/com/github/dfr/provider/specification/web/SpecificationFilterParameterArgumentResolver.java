package com.github.dfr.provider.specification.web;

import static com.github.dfr.filter.LogicType.CONJUNCTION;
import static com.github.dfr.filter.LogicType.DISJUNCTION;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.github.dfr.annotation.And;
import com.github.dfr.annotation.Conjunction;
import com.github.dfr.annotation.Disjunction;
import com.github.dfr.annotation.Filter;
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
		int annotationQuantity = countPresentAnnotations(parameter);
		if (annotationQuantity > 1) {
			throw new IllegalStateException(
					"The annotations Or, And, Conjunction and Disjunction cannot be used at the same time, as seen on parameter + "
							+ parameter.getParameterName());
		}
		return Specification.class.isAssignableFrom(parameterType) && annotationQuantity == 1;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		ConditionalStatement statement = null;

		And andAnnot;
		if ((andAnnot = parameter.getParameterAnnotation(And.class)) != null) {
			statement = logicContextProvider.createLogicContext(CONJUNCTION, andAnnot.values(), null, webRequest.getParameterMap());
		}

		Or orAnnot;
		if (statement == null && (orAnnot = parameter.getParameterAnnotation(Or.class)) != null) {
			statement = logicContextProvider.createLogicContext(DISJUNCTION, orAnnot.values(), null, webRequest.getParameterMap());
		}

		Conjunction conjAnnot;
		if (statement == null && (conjAnnot = parameter.getParameterAnnotation(Conjunction.class)) != null) {
			List<Filter[]> disjunctions = new ArrayList<>(conjAnnot.values().length);
			for (Or or : conjAnnot.values()) {
				disjunctions.add(or.values());
			}
			statement = logicContextProvider.createLogicContext(CONJUNCTION, conjAnnot.and(), disjunctions, webRequest.getParameterMap());
		}

		Disjunction disjAnnot;
		if (statement == null && (disjAnnot = parameter.getParameterAnnotation(Disjunction.class)) != null) {
			List<Filter[]> conjunctions = new ArrayList<>(disjAnnot.values().length);
			for (And and : disjAnnot.values()) {
				conjunctions.add(and.values());
			}
			statement = logicContextProvider.createLogicContext(DISJUNCTION, disjAnnot.or(), conjunctions, webRequest.getParameterMap());
		}

		return dynamicFilterResolver.convertTo(statement);
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
