package com.github.dfr.filter;

public interface FilterParameterResolver<T> {

	T convertTo(FilterLogicalContext filterLogicalContext);

}
