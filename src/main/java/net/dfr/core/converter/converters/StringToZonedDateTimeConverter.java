package net.dfr.core.converter.converters;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import net.dfr.core.converter.AbstractFormattedConverter;

public class StringToZonedDateTimeConverter extends AbstractFormattedConverter<String, ZonedDateTime, String> {

	@Override
	public ZonedDateTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, ZonedDateTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, ZonedDateTime::from);
	}

}
