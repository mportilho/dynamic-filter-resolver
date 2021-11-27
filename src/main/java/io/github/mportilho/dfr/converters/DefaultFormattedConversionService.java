package io.github.mportilho.dfr.converters;


import io.github.mportilho.dfr.converters.impl.*;

import java.sql.Timestamp;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultFormattedConversionService implements FormattedConversionService {

    private Map<ConvertMapping, FormattedConverter<?, ?, ?>> formattedConverters;

    public DefaultFormattedConversionService() {
        this.formattedConverters = loadFormattedValueConverters();
    }

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return formattedConverters.containsKey(new ConvertMapping(sourceType, targetType));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, T, F> T convert(S source, Class<T> targetType, F format) {
        FormattedConverter<S, T, F> converter = (FormattedConverter<S, T, F>) formattedConverters.get(new ConvertMapping(source.getClass(), targetType));
        return converter.convert(source, format);
    }

    /**
     * Adds the default {@link FormattedConverter}s into the new instance of this
     * type
     */
    private Map<ConvertMapping, FormattedConverter<?, ?, ?>> loadFormattedValueConverters() {
        formattedConverters = new HashMap<>();
        formattedConverters.put(new ConvertMapping(String.class, Instant.class), new StringToInstantConverter());
        formattedConverters.put(new ConvertMapping(String.class, LocalDate.class), new StringToLocalDateConverter());
        formattedConverters.put(new ConvertMapping(String.class, LocalDateTime.class), new StringToLocalDateTimeConverter());
        formattedConverters.put(new ConvertMapping(String.class, LocalTime.class), new StringToLocalTimeConverter());
        formattedConverters.put(new ConvertMapping(String.class, YearMonth.class), new StringToYearMonthConverter());
        formattedConverters.put(new ConvertMapping(String.class, OffsetDateTime.class), new StringToOffsetDateTimeConverter());
        formattedConverters.put(new ConvertMapping(String.class, OffsetTime.class), new StringToOffsetTimeConverter());
        formattedConverters.put(new ConvertMapping(String.class, Year.class), new StringToYearConverter());
        formattedConverters.put(new ConvertMapping(String.class, ZonedDateTime.class), new StringToZonedDateTimeConverter());
        formattedConverters.put(new ConvertMapping(String.class, Date.class), new StringToJavaUtilDateConverter());
        formattedConverters.put(new ConvertMapping(String.class, java.sql.Date.class), new StringToJavaSqlDateConverter());
        formattedConverters.put(new ConvertMapping(String.class, Timestamp.class), new StringToTimestampConverter());
        formattedConverters.put(new ConvertMapping(String.class, Boolean.class), new StringToBooleanConverter());
        return formattedConverters;
    }

}
