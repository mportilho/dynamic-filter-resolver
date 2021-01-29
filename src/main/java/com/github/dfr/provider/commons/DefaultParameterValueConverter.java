package com.github.dfr.provider.commons;

import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

import com.github.dfr.operator.ParameterValueConverter;
import com.github.dfr.provider.commons.converter.StringToInstantConverter;
import com.github.dfr.provider.commons.converter.StringToJavaSqlDateConverter;
import com.github.dfr.provider.commons.converter.StringToJavaUtilDateConverter;
import com.github.dfr.provider.commons.converter.StringToLocalDateConverter;
import com.github.dfr.provider.commons.converter.StringToLocalDateTimeConverter;
import com.github.dfr.provider.commons.converter.StringToLocalTimeConverter;
import com.github.dfr.provider.commons.converter.StringToMonthYearConverter;
import com.github.dfr.provider.commons.converter.StringToOffsetDateTimeConverter;
import com.github.dfr.provider.commons.converter.StringToOffsetTimeConverter;
import com.github.dfr.provider.commons.converter.StringToTimestampConverter;
import com.github.dfr.provider.commons.converter.StringToYearConverter;
import com.github.dfr.provider.commons.converter.StringToZonedDateTimeConverter;

public class DefaultParameterValueConverter implements ParameterValueConverter {

	private final ConversionService localConversionService;
	private final ConversionService conversionService;

	public DefaultParameterValueConverter() {
		this.localConversionService = loadLocalConversionService();
		this.conversionService = null;
	}

	public DefaultParameterValueConverter(ConversionService conversionService) {
		this.localConversionService = loadLocalConversionService();
		this.conversionService = conversionService;
	}

	/**
	 * 
	 * @param valueResolver
	 * @return
	 */
	private ConversionService loadLocalConversionService() {
		DefaultFormattingConversionService localConverter = new DefaultFormattingConversionService(null, true);
		localConverter.addConverter(new StringToInstantConverter());
		localConverter.addConverter(new StringToLocalDateConverter());
		localConverter.addConverter(new StringToLocalDateTimeConverter());
		localConverter.addConverter(new StringToLocalTimeConverter());
		localConverter.addConverter(new StringToMonthYearConverter());
		localConverter.addConverter(new StringToOffsetDateTimeConverter());
		localConverter.addConverter(new StringToOffsetTimeConverter());
		localConverter.addConverter(new StringToYearConverter());
		localConverter.addConverter(new StringToZonedDateTimeConverter());
		localConverter.addConverter(new StringToJavaUtilDateConverter());
		localConverter.addConverter(new StringToJavaSqlDateConverter());
		localConverter.addConverter(new StringToTimestampConverter());
		return localConverter;
	}

	/**
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <R> R convert(Object value, Class<?> expectedType, Object format) {
		if (value == null) {
			return null;
		}
		if (expectedType == null) {
			throw new IllegalStateException("The expected type for conversion must be informed");
		}
		return (R) convertValue(value, expectedType, format);
	}

	/**
	 * Convert values to the target {@link Class}
	 * 
	 * @param value
	 * @param targetClass
	 * @return The converted value if a converter was found, null is a converter was
	 *         not found or <code>null</code> if value parameter
	 */
	private Object convertValue(Object value, Class<?> targetClass, Object format) {
		if (value == null) {
			return null;
		}
		if (localConversionService.canConvert(value.getClass(), targetClass)) {
			return localConversionService.convert(value, targetClass);
		} else if (conversionService != null && conversionService.canConvert(value.getClass(), targetClass)) {
			return conversionService.convert(value, targetClass);
		}
		return value;
	}

}
