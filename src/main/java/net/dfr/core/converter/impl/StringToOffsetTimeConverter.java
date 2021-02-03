package net.dfr.core.converter.impl;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

import net.dfr.core.converter.AbstractFormattedConverter;

public class StringToOffsetTimeConverter extends AbstractFormattedConverter<String, OffsetTime, String> {

	@Override
	public OffsetTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_TIME_FORMATTER.parse(source, OffsetTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, OffsetTime::from);
	}

}
