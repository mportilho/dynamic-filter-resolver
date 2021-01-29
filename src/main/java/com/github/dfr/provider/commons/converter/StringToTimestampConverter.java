package com.github.dfr.provider.commons.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToTimestampConverter extends AbstractFormattedValueConverter<String, Timestamp, String> {

	@Override
	public Timestamp convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			LocalDateTime dateTime = DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, LocalDateTime::from);
			return Timestamp.valueOf(dateTime);
		}
		LocalDateTime dateTime = cache(format, DateTimeFormatter::ofPattern).parse(source, LocalDateTime::from);
		return Timestamp.valueOf(dateTime);
	}

}
