package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.util.Objects;

public class StringToFloatConverter implements FormattedConverter<String, Float, String> {

    @Override
    public Float convert(String source, String format) {
        return Float.valueOf(Objects.requireNonNull(source));
    }
}
