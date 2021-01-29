package com.github.dfr.filter;

public interface DynamicFilterResolver<T> {

	T convertTo(FilterLogicContext filterLogicContext);

}
