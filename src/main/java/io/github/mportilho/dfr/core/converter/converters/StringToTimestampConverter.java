package io.github.mportilho.dfr.core.converter.converters;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.github.mportilho.dfr.core.converter.AbstractFormattedConverter;

public class StringToTimestampConverter extends AbstractFormattedConverter<String, Timestamp, String> {

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
