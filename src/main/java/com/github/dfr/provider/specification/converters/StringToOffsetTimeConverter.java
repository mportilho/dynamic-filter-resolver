package com.github.dfr.provider.specification.converters;

import java.time.OffsetTime;

import org.springframework.core.convert.converter.Converter;

import com.github.dfr.provider.specification.converters.utils.DateConverterUtils;

public class StringToOffsetTimeConverter implements Converter<String, OffsetTime> {

	@Override
	public OffsetTime convert(String source) {
		return DateConverterUtils.GENERIC_TIME_FORMATTER.parse(source, OffsetTime::from);
	}

}
