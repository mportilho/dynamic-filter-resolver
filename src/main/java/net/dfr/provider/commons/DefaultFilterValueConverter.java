package net.dfr.provider.commons;

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

import net.dfr.operator.FilterValueConverter;
import net.dfr.provider.commons.converter.StringToInstantConverter;
import net.dfr.provider.commons.converter.StringToJavaSqlDateConverter;
import net.dfr.provider.commons.converter.StringToJavaUtilDateConverter;
import net.dfr.provider.commons.converter.StringToLocalDateConverter;
import net.dfr.provider.commons.converter.StringToLocalDateTimeConverter;
import net.dfr.provider.commons.converter.StringToLocalTimeConverter;
import net.dfr.provider.commons.converter.StringToOffsetDateTimeConverter;
import net.dfr.provider.commons.converter.StringToOffsetTimeConverter;
import net.dfr.provider.commons.converter.StringToTimestampConverter;
import net.dfr.provider.commons.converter.StringToYearConverter;
import net.dfr.provider.commons.converter.StringToYearMonthConverter;
import net.dfr.provider.commons.converter.StringToZonedDateTimeConverter;

public class DefaultFilterValueConverter implements FilterValueConverter {

	Map<Pair<Class<?>, Class<?>>, AbstractFormattedValueConverter<?, ?, String>> abstractFormattedValueConverters;
	private final ConversionService conversionService;

	public DefaultFilterValueConverter() {
		this.abstractFormattedValueConverters = loadFormattedValueConverters();
		this.conversionService = null;
	}

	public DefaultFilterValueConverter(ConversionService conversionService) {
		this.abstractFormattedValueConverters = loadFormattedValueConverters();
		this.conversionService = conversionService;
	}

	/**
	 * 
	 * @param valueResolver
	 * @return
	 */
	private Map<Pair<Class<?>, Class<?>>, AbstractFormattedValueConverter<?, ?, String>> loadFormattedValueConverters() {
		abstractFormattedValueConverters = new HashMap<>();
		abstractFormattedValueConverters.put(Pair.of(String.class, Instant.class), new StringToInstantConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, LocalDate.class), new StringToLocalDateConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, LocalDateTime.class), new StringToLocalDateTimeConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, LocalTime.class), new StringToLocalTimeConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, YearMonth.class), new StringToYearMonthConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, OffsetDateTime.class), new StringToOffsetDateTimeConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, OffsetTime.class), new StringToOffsetTimeConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, Year.class), new StringToYearConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, ZonedDateTime.class), new StringToZonedDateTimeConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, Date.class), new StringToJavaUtilDateConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, java.sql.Date.class), new StringToJavaSqlDateConverter());
		abstractFormattedValueConverters.put(Pair.of(String.class, Timestamp.class), new StringToTimestampConverter());
		return abstractFormattedValueConverters;
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
		AbstractFormattedValueConverter abstractFormattedValueConverter;
		if ((abstractFormattedValueConverter = this.abstractFormattedValueConverters.get(Pair.of(String.class, targetClass))) != null) {
			return abstractFormattedValueConverter.convert(value, format);
		} else if (conversionService != null && conversionService.canConvert(value.getClass(), targetClass)) {
			return conversionService.convert(value, targetClass);
		}
		return value;
	}

}
