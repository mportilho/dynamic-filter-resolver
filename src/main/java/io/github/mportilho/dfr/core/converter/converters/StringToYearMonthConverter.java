package io.github.mportilho.dfr.core.converter.converters;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import io.github.mportilho.dfr.core.converter.AbstractFormattedConverter;

public class StringToYearMonthConverter extends AbstractFormattedConverter<String, YearMonth, String> {

	@Override
	public YearMonth convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_MONTH_YEAR_FORMATTER.parse(source, YearMonth::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, YearMonth::from);
	}

}
