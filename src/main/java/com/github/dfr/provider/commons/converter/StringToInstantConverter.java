package com.github.dfr.provider.commons.converter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.github.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToInstantConverter extends AbstractFormattedValueConverter<String, Instant, String> {

	@Override
	public Instant convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return Instant.parse(source);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, Instant::from);
	}

}
