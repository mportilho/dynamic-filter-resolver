package net.dfr.provider.commons.converter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import net.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToJavaSqlDateConverter extends AbstractFormattedValueConverter<String, Date, String> {

	@Override
	public Date convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			LocalDate localDate = DateConverterUtils.GENERIC_DATE_FORMATTER.parse(source, LocalDate::from);
			return Date.valueOf(localDate);
		}
		LocalDate localDate = cache(format, DateTimeFormatter::ofPattern).parse(source, LocalDate::from);
		return Date.valueOf(localDate);
	}

}
