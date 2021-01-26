package com.github.dfr.provider.specification.converters;

import java.time.LocalTime;

import org.springframework.core.convert.converter.Converter;

import com.github.dfr.provider.specification.converters.utils.DateConverterUtils;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

	@Override
	public LocalTime convert(String source) {
		return DateConverterUtils.GENERIC_TIME_FORMATTER.parse(source, LocalTime::from);
	}

}
