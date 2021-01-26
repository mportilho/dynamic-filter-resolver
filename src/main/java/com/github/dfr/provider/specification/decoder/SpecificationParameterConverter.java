package com.github.dfr.provider.specification.decoder;

import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.StringValueResolver;

import com.github.dfr.decoder.ParameterConverter;
import com.github.dfr.provider.specification.converters.StringToInstantConverter;
import com.github.dfr.provider.specification.converters.StringToLocalDateConverter;
import com.github.dfr.provider.specification.converters.StringToLocalDateTimeConverter;
import com.github.dfr.provider.specification.converters.StringToLocalTimeConverter;
import com.github.dfr.provider.specification.converters.StringToMonthYearConverter;
import com.github.dfr.provider.specification.converters.StringToOffsetDateTimeConverter;
import com.github.dfr.provider.specification.converters.StringToOffsetTimeConverter;
import com.github.dfr.provider.specification.converters.StringToYearConverter;
import com.github.dfr.provider.specification.converters.StringToZonedDateTimeConverter;

public class SpecificationParameterConverter implements ParameterConverter {

	private final ConversionService localConversionService;
	private final ConversionService conversionService;
	private final StringValueResolver valueResolver;

	public SpecificationParameterConverter() {
		this.localConversionService = loadLocalConversionService(null);
		this.conversionService = new DefaultFormattingConversionService(null, true);
		this.valueResolver = null;
	}

	public SpecificationParameterConverter(ConversionService conversionService) {
		this.localConversionService = loadLocalConversionService(null);
		this.conversionService = conversionService;
		this.valueResolver = null;
	}

	public SpecificationParameterConverter(StringValueResolver valueResolver) {
		this.localConversionService = loadLocalConversionService(valueResolver);
		this.conversionService = new DefaultFormattingConversionService(valueResolver, true);
		this.valueResolver = valueResolver;
	}

	public SpecificationParameterConverter(ConversionService conversionService, StringValueResolver valueResolver) {
		this.localConversionService = loadLocalConversionService(valueResolver);
		this.conversionService = conversionService;
		this.valueResolver = valueResolver;
	}

	/**
	 * 
	 * @param valueResolver
	 * @return
	 */
	private ConversionService loadLocalConversionService(StringValueResolver valueResolver) {
		DefaultFormattingConversionService localConverter = new DefaultFormattingConversionService(valueResolver, true);
		localConverter.addConverter(new StringToInstantConverter());
		localConverter.addConverter(new StringToLocalDateConverter());
		localConverter.addConverter(new StringToLocalDateTimeConverter());
		localConverter.addConverter(new StringToLocalTimeConverter());
		localConverter.addConverter(new StringToMonthYearConverter());
		localConverter.addConverter(new StringToOffsetDateTimeConverter());
		localConverter.addConverter(new StringToOffsetTimeConverter());
		localConverter.addConverter(new StringToYearConverter());
		localConverter.addConverter(new StringToZonedDateTimeConverter());
		return localConverter;
	}

	/**
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <R> R convert(Object value, Class<?> expectedType) {
		if (value == null) {
			return null;
		}
		if (expectedType == null) {
			throw new IllegalStateException("The expected type for conversion must be informed");
		}
		return (R) convertValue(value, expectedType);
	}

	/**
	 * Convert values to the target {@link Class}
	 * 
	 * @param value
	 * @param targetClass
	 * @return The converted value if a converter was found, null is a converter was
	 *         not found or <code>null</code> if value parameter
	 */
	private Object convertValue(Object value, Class<?> targetClass) {
		if (value == null) {
			return null;
		}
		if (localConversionService.canConvert(value.getClass(), targetClass)) {
			return localConversionService.convert(applyValueResolver(value), targetClass);
		} else if (conversionService.canConvert(value.getClass(), targetClass)) {
			return conversionService.convert(applyValueResolver(value), targetClass);
		}
		return value;
	}

	private Object applyValueResolver(Object value) {
		if (valueResolver != null && value instanceof String) {
			return valueResolver.resolveStringValue((String) value);
		}
		return value;
	}

}
