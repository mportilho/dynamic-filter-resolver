package io.github.mportilho.dfr.core.statement;

import java.lang.annotation.Annotation;
import java.util.Map;

import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.FilterOperator;

public interface ConditionalStatementProvider {

	<K, V> ConditionalStatement createConditionalStatements(Class<?> parameterInterface, Annotation[] parameterAnnotations,
			Map<K, V[]> parametersMap);

	default <K, V> FilterParameter decorateFilterParameter(FilterParameter filterParameter, Map<K, V[]> parametersMap) {
		return filterParameter;
	}

	@SuppressWarnings("rawtypes")
	default boolean operatorAcceptsNullValues(Class<? extends FilterOperator> clazz) {
		return false;
	}

}
