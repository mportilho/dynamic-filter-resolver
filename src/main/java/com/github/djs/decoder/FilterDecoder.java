package com.github.djs.decoder;

import java.util.Map;

import com.github.djs.filter.ParameterFilterMetadata;

public interface FilterDecoder<T> {

	T decode(ParameterFilterMetadata metadata, ValueConverter valueConverter, Map<String, Object> sharedContext);

}
