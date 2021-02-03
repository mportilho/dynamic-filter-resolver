package net.dfr.filter;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface ConditionalStatementProvider<T> {

	<K, V> ConditionalStatement createConditionalStatements(Class<T> parameterInterface, Annotation[] parameterAnnotations,
			Map<K, V[]> parametersMap);

	default <K, V> FilterParameter decorateFilterParameter(FilterParameter filterParameter, Map<K, V[]> parametersMap) {
		return filterParameter;
	}

}
