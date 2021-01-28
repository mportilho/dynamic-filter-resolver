package com.github.dfr.provider.specification.converters;

import java.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String source) {
		return DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, LocalDateTime::from);
	}

}
