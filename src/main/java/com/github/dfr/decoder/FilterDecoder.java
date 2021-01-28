package com.github.dfr.decoder;

import java.util.Map;

import com.github.dfr.filter.FilterParameter;

public interface FilterDecoder<T> {

	T decode(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter, Map<String, Object> sharedContext);

}
