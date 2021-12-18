package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class StringToBigDecimalConverter implements FormattedConverter<String, BigDecimal, String> {

    @Override
    public BigDecimal convert(String source, String format) {
        if (isNullOrBlank(format)) {
            return new BigDecimal(source);
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);
        decimalFormat.setParseBigDecimal(true);
        try {
            return (BigDecimal) decimalFormat.parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Error converting String to BigDecimal", e);
        }
    }
}
