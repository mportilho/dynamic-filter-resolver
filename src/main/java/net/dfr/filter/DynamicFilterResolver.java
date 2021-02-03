package net.dfr.filter;

public interface DynamicFilterResolver<T> {

	T convertTo(ConditionalStatement conditionalStatement);

	default T responseDecorator(T response) {
		return response;
	}

}
