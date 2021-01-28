package com.github.dfr.decoder;

import java.util.Map;
import java.util.function.Function;

import com.github.dfr.filter.FilterParameter;

public interface FilterDecoder<T> {

	T decode(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter, Map<String, Object> sharedContext);

	default <P, R> R transformNonNull(P value, Function<P, R> function) {
		if (value != null) {
			return function.apply(value);
		}
		return null;
	}

}
