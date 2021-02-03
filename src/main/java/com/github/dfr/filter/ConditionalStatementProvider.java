package com.github.dfr.filter;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface ConditionalStatementProvider<T> {

	ConditionalStatement createConditionalStatements(Class<T> parameterInterface, Annotation[] parameterAnnotations,
			Map<Object, Object[]> parametersMap);

	default FilterParameter decorateFilterParameter(FilterParameter filterParameter, Map<Object, Object[]> parametersMap) {
		return filterParameter;
	}

}
