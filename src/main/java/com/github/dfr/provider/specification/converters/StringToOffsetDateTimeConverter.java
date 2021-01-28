package com.github.dfr.provider.specification.converters;

import java.time.OffsetDateTime;

import org.springframework.core.convert.converter.Converter;

public class StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

	@Override
	public OffsetDateTime convert(String source) {
		return DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, OffsetDateTime::from);
	}

}
