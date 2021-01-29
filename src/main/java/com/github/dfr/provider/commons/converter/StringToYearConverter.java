package com.github.dfr.provider.commons.converter;

import java.time.Year;
import java.time.format.DateTimeFormatter;

import com.github.dfr.provider.commons.AbstractFormattedValueConverter;

public class StringToYearConverter extends AbstractFormattedValueConverter<String, Year, String> {

	@Override
	public Year convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			return Year.parse(source);
		}
		return cache(format, DateTimeFormatter::ofPattern).parse(source, Year::from);
	}

}
