package net.dfr.core.converter;

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

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import net.dfr.core.Pair;
import net.dfr.core.converter.impl.StringToInstantConverter;
import net.dfr.core.converter.impl.StringToJavaSqlDateConverter;
import net.dfr.core.converter.impl.StringToJavaUtilDateConverter;
import net.dfr.core.converter.impl.StringToLocalDateConverter;
import net.dfr.core.converter.impl.StringToLocalDateTimeConverter;
import net.dfr.core.converter.impl.StringToLocalTimeConverter;
import net.dfr.core.converter.impl.StringToOffsetDateTimeConverter;
import net.dfr.core.converter.impl.StringToOffsetTimeConverter;
import net.dfr.core.converter.impl.StringToTimestampConverter;
import net.dfr.core.converter.impl.StringToYearConverter;
import net.dfr.core.converter.impl.StringToYearMonthConverter;
import net.dfr.core.converter.impl.StringToZonedDateTimeConverter;

public class DefaultFilterValueConverter implements FilterValueConverter {

	private Map<Pair<Class<?>, Class<?>>, FormattedConverter<?, ?, String>> formattedConverters;
	private final ConversionService conversionService;

	public DefaultFilterValueConverter() {
		this.formattedConverters = loadFormattedValueConverters();
		this.conversionService = DefaultConversionService.getSharedInstance();
	}

	public DefaultFilterValueConverter(ConversionService conversionService) {
		this.formattedConverters = loadFormattedValueConverters();
		this.conversionService = conversionService;
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

}
