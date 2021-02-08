package io.github.mportilho.dfr.core.converter.converters;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import io.github.mportilho.dfr.core.converter.AbstractFormattedConverter;

public class StringToLocalTimeConverter extends AbstractFormattedConverter<String, LocalTime, String> {

	@Override
	public LocalTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_TIME_FORMATTER.parse(source, LocalTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, LocalTime::from);
	}

}
