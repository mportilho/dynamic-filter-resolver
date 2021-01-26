package com.github.dfr.decoder;

import java.util.Map;

import com.github.dfr.filter.ParameterFilterMetadata;

public interface FilterDecoder<T> {

	T decode(ParameterFilterMetadata metadata, ParameterConverter parameterConverter, Map<String, Object> sharedContext);

	default boolean validateParameterCount(ParameterFilterMetadata metadata, int count, boolean errorOnNullOrEmpty) {
		if (metadata.getValues() == null || metadata.getValues().length == 0) {
			if (errorOnNullOrEmpty) {
				throw new IllegalArgumentException(String.format("Required value(s) not found for '%s' decoder", this.getClass().getSimpleName()));
			}
			return true;
		}
		return metadata.getValues().length == count;
	}

}
