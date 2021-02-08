package net.dfr.core.converter.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import net.dfr.core.converter.AbstractFormattedConverter;

public class StringToLocalDateConverter extends AbstractFormattedConverter<String, LocalDate, String> {

	@Override
	public LocalDate convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_DATE_FORMATTER.parse(source, LocalDate::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, LocalDate::from);
	}

}
