package com.github.dfr.provider.commons;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

public abstract class AbstractFormattedValueConverter<S, T, F> implements FormattedValueConverter<S, T, F> {

	private Map<F, Object> cache = new WeakHashMap<>();

	@Override
	@SuppressWarnings("unchecked")
	public <U> U cache(F format, Function<F, U> supplier) {
		return (U) cache.computeIfAbsent(format, supplier);
	}

}
