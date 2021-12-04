package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.util.Objects;

public class StringToIntegerConverter implements FormattedConverter<String, Integer, String> {

    @Override
    public Integer convert(String source, String format) {
        return Integer.valueOf(Objects.requireNonNull(source));
    }
}
