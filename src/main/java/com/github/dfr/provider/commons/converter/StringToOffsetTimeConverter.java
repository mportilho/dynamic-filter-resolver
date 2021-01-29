package com.github.dfr.provider.commons.converter;

import java.time.OffsetTime;

import org.springframework.core.convert.converter.Converter;

public class StringToOffsetTimeConverter implements Converter<String, OffsetTime> {

	@Override
	public OffsetTime convert(String source) {
		return DateConverterUtils.GENERIC_TIME_FORMATTER.parse(source, OffsetTime::from);
	}

}
