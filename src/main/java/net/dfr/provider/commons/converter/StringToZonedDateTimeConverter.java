package net.dfr.provider.commons.converter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import net.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToZonedDateTimeConverter extends AbstractFormattedValueConverter<String, ZonedDateTime, String> {

	@Override
	public ZonedDateTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, ZonedDateTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, ZonedDateTime::from);
	}

}
