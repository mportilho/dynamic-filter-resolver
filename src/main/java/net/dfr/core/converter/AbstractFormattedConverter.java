package net.dfr.core.converter;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

public abstract class AbstractFormattedConverter<S, T, F> implements FormattedConverter<S, T, F> {

	private Map<F, Object> cache = new WeakHashMap<>();

	@SuppressWarnings("unchecked")
	protected <U> U cache(F format, Function<F, U> supplier) {
		return (U) cache.computeIfAbsent(format, supplier);
	}

}
