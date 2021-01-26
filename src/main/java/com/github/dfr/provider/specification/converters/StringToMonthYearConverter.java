package com.github.dfr.provider.specification.converters;

import java.time.YearMonth;

import org.springframework.core.convert.converter.Converter;

import com.github.dfr.provider.specification.converters.utils.DateConverterUtils;

public class StringToMonthYearConverter implements Converter<String, YearMonth> {

	@Override
	public YearMonth convert(String source) {
		return DateConverterUtils.GENERIC_MONTH_YEAR_FORMATTER.parse(source, YearMonth::from);
	}

}
