package net.dfr.core.statement;

import java.lang.annotation.Annotation;
import java.util.Map;

import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.FilterOperator;

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
