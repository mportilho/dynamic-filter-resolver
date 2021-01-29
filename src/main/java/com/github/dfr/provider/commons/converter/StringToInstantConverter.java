package com.github.dfr.provider.commons.converter;

import java.time.Instant;

import org.springframework.core.convert.converter.Converter;

public class StringToInstantConverter implements Converter<String, Instant> {

	@Override
	public Instant convert(String source) {
		return Instant.parse(source);
	}

}
