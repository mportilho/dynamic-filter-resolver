package net.dfr.provider.commons.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToLocalDateTimeConverter extends AbstractFormattedValueConverter<String, LocalDateTime, String> {

	@Override
	public LocalDateTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, LocalDateTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, LocalDateTime::from);
	}

}
