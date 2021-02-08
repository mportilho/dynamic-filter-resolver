package io.github.mportilho.dfr.core.converter.converters;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import io.github.mportilho.dfr.core.converter.AbstractFormattedConverter;

public class StringToInstantConverter extends AbstractFormattedConverter<String, Instant, String> {

	@Override
	public Instant convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, Instant::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, Instant::from);
	}

}
