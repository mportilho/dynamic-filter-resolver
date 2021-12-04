package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.util.Objects;

public class StringToDoubleConverter implements FormattedConverter<String, Double, String> {

    @Override
    public Double convert(String source, String format) {
        return Double.valueOf(Objects.requireNonNull(source));
    }

}
