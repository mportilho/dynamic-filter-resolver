package com.github.dfr.provider.commons.converter;

import java.time.ZonedDateTime;

import org.springframework.core.convert.converter.Converter;

public class StringToZonedDateTimeConverter implements Converter<String, ZonedDateTime> {

	@Override
	public ZonedDateTime convert(String source) {
		return DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, ZonedDateTime::from);
	}

}
