package io.github.mportilho.dfr.core.filter;

import java.util.Map;

import io.github.mportilho.dfr.core.statement.ConditionalStatement;

public interface DynamicFilterResolver<T> {

	<R extends T, K, V> R convertTo(ConditionalStatement conditionalStatement, Map<K, V> context);

	default <R extends T> R convertTo(ConditionalStatement conditionalStatement) {
		return convertTo(conditionalStatement, null);
	}

	default <R extends T, K, V> R responseDecorator(R response, Map<K, V> context) {
		return response;
	}

}
