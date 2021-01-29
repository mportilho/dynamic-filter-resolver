package com.github.dfr.provider.commons.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;

public class StringToTimestampConverter implements Converter<String, Timestamp> {

	@Override
	public Timestamp convert(String source) {
		LocalDateTime dateTime = DateConverterUtils.GENERIC_DATETIME_FORMATTER.parse(source, LocalDateTime::from);
		return Timestamp.valueOf(dateTime);
	}

}
