package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.math.BigDecimal;
import java.util.Objects;

public class StringToBigDecimalConverter implements FormattedConverter<String, BigDecimal, String> {

    @Override
    public BigDecimal convert(String source, String format) {
        return new BigDecimal(Objects.requireNonNull(source));
    }
}
