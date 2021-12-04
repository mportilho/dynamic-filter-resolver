package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.util.Objects;

public class StringToShortConverter implements FormattedConverter<String, Short, String> {

    @Override
    public Short convert(String source, String format) {
        return Short.valueOf(Objects.requireNonNull(source));
    }
}
