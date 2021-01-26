package com.github.dfr.decoder;

import java.util.Map;

import com.github.dfr.filter.ParameterFilterMetadata;

public interface FilterDecoder<T> {

	T decode(ParameterFilterMetadata metadata, ValueConverter valueConverter, Map<String, Object> sharedContext);

}
