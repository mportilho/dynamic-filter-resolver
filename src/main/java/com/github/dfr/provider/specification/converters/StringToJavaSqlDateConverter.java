package com.github.dfr.provider.specification.converters;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;

public class StringToJavaSqlDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		LocalDate localDate = DateConverterUtils.GENERIC_DATE_FORMATTER.parse(source, LocalDate::from);
		return Date.valueOf(localDate);
	}

}
