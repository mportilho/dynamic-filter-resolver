package net.dfr.core.filter;

import java.util.Map;

import net.dfr.core.statement.ConditionalStatement;

public interface DynamicFilterResolver<T> {

	<K, V> T convertTo(ConditionalStatement conditionalStatement, Map<K, V> context);

	default T convertTo(ConditionalStatement conditionalStatement) {
		return convertTo(conditionalStatement, null);
	}

	default <K, V> T responseDecorator(T response, Map<K, V> context) {
		return response;
	}

}
