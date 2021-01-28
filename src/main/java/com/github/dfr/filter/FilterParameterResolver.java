package com.github.dfr.filter;

public interface FilterParameterResolver<T> {

	T convertTo(LogicalContext logicalContext);

}
