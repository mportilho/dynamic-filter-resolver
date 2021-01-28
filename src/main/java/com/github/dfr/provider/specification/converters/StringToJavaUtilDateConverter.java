package com.github.dfr.provider.specification.converters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringToJavaUtilDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		LocalDateTime localDateTime = DateConverterUtils.GENERIC_DATETIME_FORMATTER_PADDING_HOURS.parse(source, LocalDateTime::from);
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

}
