package io.github.mportilho.dfr.core.operator;

import java.util.function.Function;

import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.FilterParameter;

public interface FilterOperator<T> {

	T createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter);

	default <P, R> R transformNonNull(P value, Function<P, R> function) {
		if (value != null) {
			return function.apply(value);
		}
		return null;
	}

}
