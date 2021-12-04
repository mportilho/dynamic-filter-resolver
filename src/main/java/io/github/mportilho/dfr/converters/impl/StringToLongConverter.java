package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.util.Objects;

public class StringToLongConverter implements FormattedConverter<String, Long, String> {

    @Override
    public Long convert(String source, String format) {
        return Long.parseLong(Objects.requireNonNull(source));
    }

}
