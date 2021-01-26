package com.github.dfr.filter;

import java.util.Collection;

public interface ParameterFilter<T> {

	T convertTo(Collection<ParameterFilterMetadata> parameters);

}
