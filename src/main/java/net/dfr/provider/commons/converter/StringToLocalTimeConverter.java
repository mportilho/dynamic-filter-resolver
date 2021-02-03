package net.dfr.provider.commons.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import net.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToLocalTimeConverter extends AbstractFormattedValueConverter<String, LocalTime, String> {

	@Override
	public LocalTime convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_TIME_FORMATTER.parse(source, LocalTime::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, LocalTime::from);
	}

}
