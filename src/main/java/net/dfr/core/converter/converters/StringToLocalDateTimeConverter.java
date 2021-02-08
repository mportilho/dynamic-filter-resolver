package net.dfr.core.converter.converters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.dfr.core.converter.AbstractFormattedConverter;

public class StringToLocalDateTimeConverter extends AbstractFormattedConverter<String, LocalDateTime, String> {

	@Override
	public LocalDateTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_DATETIME_FORMATTER_PADDING_HOURS.parse(source, LocalDateTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, LocalDateTime::from);
	}

}
