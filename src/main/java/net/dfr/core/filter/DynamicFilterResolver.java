package net.dfr.core.filter;

import net.dfr.core.statement.ConditionalStatement;

public interface DynamicFilterResolver<T> {

	T convertTo(ConditionalStatement conditionalStatement);

	default T responseDecorator(T response) {
		return response;
	}

}
