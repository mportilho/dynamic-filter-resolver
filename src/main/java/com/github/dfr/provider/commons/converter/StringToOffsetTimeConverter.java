package com.github.dfr.provider.commons.converter;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

import com.github.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToOffsetTimeConverter extends AbstractFormattedValueConverter<String, OffsetTime, String> {

	@Override
	public OffsetTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_TIME_FORMATTER.parse(source, OffsetTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, OffsetTime::from);
	}

}
