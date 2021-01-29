package com.github.dfr.provider.commons.converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import com.github.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToYearMonthConverter extends AbstractFormattedValueConverter<String, YearMonth, String> {

	@Override
	public YearMonth convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return DateConverterUtils.GENERIC_MONTH_YEAR_FORMATTER.parse(source, YearMonth::from);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, YearMonth::from);
	}

}
