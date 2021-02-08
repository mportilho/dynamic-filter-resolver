package io.github.mportilho.dfr.core.converter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.github.mportilho.dfr.core.converter.converters.StringToInstantConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToJavaSqlDateConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToJavaUtilDateConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToLocalDateConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToLocalDateTimeConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToLocalTimeConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToOffsetDateTimeConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToOffsetTimeConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToTimestampConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToYearConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToYearMonthConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToZonedDateTimeConverter;

public class DefaultFilterValueConverter implements FilterValueConverter {

	private static final FormattedConversionService DEFAULT_FORMATTED_CONVERSION_SERVICE = createDefaultFormmatedConversionService();

	private Map<Pair<Class<?>, Class<?>>, FormattedConverter<?, ?, String>> formattedConverters;
	private final FormattedConversionService conversionService;

	public DefaultFilterValueConverter() {
		this.formattedConverters = loadFormattedValueConverters();
		this.conversionService = DEFAULT_FORMATTED_CONVERSION_SERVICE;
	}

	public DefaultFilterValueConverter(FormattedConversionService conversionService) {
		this.formattedConverters = loadFormattedValueConverters();
		this.conversionService = conversionService;
	}

	/**
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <R> R convert(Object value, Class<?> expectedType, String format) {
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertValue(Object value, Class<?> targetClass, Object format) {
		if (value == null) {
			return null;
		}
		FormattedConverter converter;
		if ((converter = this.formattedConverters.get(Pair.of(String.class, targetClass))) != null) {
			return converter.convert(value, format);
		} else if (conversionService != null && conversionService.canConvert(value.getClass(), targetClass)) {
			return conversionService.convert(value, targetClass);
		}
		return value;
	}

	/**
	 * 
	 * @param valueResolver
	 * @return
	 */
	private Map<Pair<Class<?>, Class<?>>, FormattedConverter<?, ?, String>> loadFormattedValueConverters() {
		formattedConverters = new HashMap<>();
		formattedConverters.put(Pair.of(String.class, Instant.class), new StringToInstantConverter());
		formattedConverters.put(Pair.of(String.class, LocalDate.class), new StringToLocalDateConverter());
		formattedConverters.put(Pair.of(String.class, LocalDateTime.class), new StringToLocalDateTimeConverter());
		formattedConverters.put(Pair.of(String.class, LocalTime.class), new StringToLocalTimeConverter());
		formattedConverters.put(Pair.of(String.class, YearMonth.class), new StringToYearMonthConverter());
		formattedConverters.put(Pair.of(String.class, OffsetDateTime.class), new StringToOffsetDateTimeConverter());
		formattedConverters.put(Pair.of(String.class, OffsetTime.class), new StringToOffsetTimeConverter());
		formattedConverters.put(Pair.of(String.class, Year.class), new StringToYearConverter());
		formattedConverters.put(Pair.of(String.class, ZonedDateTime.class), new StringToZonedDateTimeConverter());
		formattedConverters.put(Pair.of(String.class, Date.class), new StringToJavaUtilDateConverter());
		formattedConverters.put(Pair.of(String.class, java.sql.Date.class), new StringToJavaSqlDateConverter());
		formattedConverters.put(Pair.of(String.class, Timestamp.class), new StringToTimestampConverter());
		return formattedConverters;
	}

	private static final FormattedConversionService createDefaultFormmatedConversionService() {
		return new FormattedConversionService() {
			@Override
			@SuppressWarnings("unchecked")
			public <T> T convert(Object source, Class<T> targetType) {
				return (T) source;
			}

			@Override
			public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
				return false;
			}
		};
	}

}
