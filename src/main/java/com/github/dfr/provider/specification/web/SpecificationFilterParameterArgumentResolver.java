package com.github.dfr.provider.specification.web;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.github.dfr.filter.LogicType;
import com.github.dfr.filter.LogicalContext;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.FilterParameterResolver;
import com.github.dfr.filter.CorrelatedFilterParameter;

public class SpecificationFilterParameterArgumentResolver implements HandlerMethodArgumentResolver {

	private StringValueResolver stringValueResolver;
	private FilterParameterResolver<Specification<?>> parameterFilter;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> parameterType = parameter.getParameterType();
		int annotationQuantity = countPresentAnnotations(parameter);
		if (annotationQuantity > 1) {
			throw new IllegalStateException(
					"The annotations Or, And, Conjunction and Disjunction cannot be used at the same time, found on parameter + "
							+ parameter.getParameterName());
		}
		return Specification.class.isAssignableFrom(parameterType) && annotationQuantity == 1;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		LogicalContext logicWrapper = null;

		And andAnnot;
		if ((andAnnot = parameter.getParameterAnnotation(And.class)) != null) {
			logicWrapper = createConnectiveLogicWrapper(LogicType.CONJUNCTION, andAnnot.values(), null, webRequest.getParameterMap());
		}

		Or orAnnot;
		if (logicWrapper == null && (orAnnot = parameter.getParameterAnnotation(Or.class)) != null) {
			logicWrapper = createConnectiveLogicWrapper(LogicType.DISJUNCTION, orAnnot.values(), null, webRequest.getParameterMap());
		}

		Conjunction conjAnnot;
		if (logicWrapper == null && (conjAnnot = parameter.getParameterAnnotation(Conjunction.class)) != null) {
			List<Filter[]> filtersList = new ArrayList<>(conjAnnot.values().length);
			for (Or or : conjAnnot.values()) {
				filtersList.add(or.values());
			}
			logicWrapper = createConnectiveLogicWrapper(LogicType.CONJUNCTION, conjAnnot.and(), filtersList, webRequest.getParameterMap());
		}

		Disjunction disjAnnot;
		if (logicWrapper == null && (disjAnnot = parameter.getParameterAnnotation(Disjunction.class)) != null) {
			List<Filter[]> filtersList = new ArrayList<>(disjAnnot.values().length);
			for (And and : disjAnnot.values()) {
				filtersList.add(and.values());
			}
			logicWrapper = createConnectiveLogicWrapper(LogicType.DISJUNCTION, disjAnnot.or(), filtersList, webRequest.getParameterMap());
		}

		return parameterFilter.convertTo(logicWrapper);
	}

	private <T> LogicalContext createConnectiveLogicWrapper(LogicType logicType, Filter[] currentLogicTypeFilters,
			List<Filter[]> oppositeLogicTypeFilters, Map<String, T[]> parametersMap) {
		List<CorrelatedFilterParameter> oppositeLogicParameterList = new ArrayList<>();
		if (oppositeLogicTypeFilters != null) {
			for (Filter[] filters : oppositeLogicTypeFilters) {
				List<FilterParameter> parameters = createWrappers(filters, parametersMap);
				if (!parameters.isEmpty()) {
					oppositeLogicParameterList.add(new CorrelatedFilterParameter(logicType.getOppositeLogicType(), parameters));
				}
			}
		}

		List<FilterParameter> parameterList = createWrappers(currentLogicTypeFilters, parametersMap);
		if (oppositeLogicParameterList.isEmpty() && parameterList.isEmpty()) {
			return null;
		}
		CorrelatedFilterParameter correlatedFilterParameter = new CorrelatedFilterParameter(logicType, parameterList);
		return new LogicalContext(logicType, correlatedFilterParameter, oppositeLogicParameterList);
	}

	/**
	 * 
	 * @param <T>
	 * @param filters
	 * @param parametersMap
	 * @return
	 */
	private <T> List<FilterParameter> createWrappers(Filter[] filters, Map<String, T[]> parametersMap) {
		List<FilterParameter> filterParameters = new ArrayList<>();
		if (filters == null || parametersMap == null || parametersMap.isEmpty()) {
			return filterParameters;
		}

		for (Filter filter : filters) {
			boolean foundAnyParameter = false;
			for (String filterParams : filter.parameters()) {
				if (parametersMap.keySet().contains(filterParams)) {
					foundAnyParameter = true;
					break;
				}
			}

			if (foundAnyParameter) {
				Object values[] = new Object[filter.parameters().length];
				for (int i = 0; i < values.length; i++) {
					Object[] paramValues = parametersMap.get(filter.parameters()[i]);
					if (paramValues != null && paramValues.length != 0) {
						if (paramValues.length == 1) {
							values[i] = paramValues[0];
						} else {
							values[i] = paramValues;
						}
					} else {
						values[i] = null;
					}

				}
				filterParameters.add(
						new FilterParameter(filter.path(), filter.parameters(), filter.targetType(), filter.decoder(), values, filter.formats()));
			}
		}
		return filterParameters;
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
