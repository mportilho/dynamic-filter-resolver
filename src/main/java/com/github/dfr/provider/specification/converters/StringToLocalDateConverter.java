package com.github.dfr.provider.specification.converters;

import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;

import com.github.dfr.provider.specification.converters.utils.DateConverterUtils;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

	@Override
	public LocalDate convert(String source) {
		return DateConverterUtils.GENERIC_DATE_FORMATTER.parse(source, LocalDate::from);
	}

}
