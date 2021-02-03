package net.dfr.provider.commons.converter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import net.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToOffsetDateTimeConverter extends AbstractFormattedValueConverter<String, OffsetDateTime, String> {

	@Override
	public OffsetDateTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, OffsetDateTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, OffsetDateTime::from);
	}

}
