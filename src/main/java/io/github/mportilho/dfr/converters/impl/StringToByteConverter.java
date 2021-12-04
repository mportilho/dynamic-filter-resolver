package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.util.Objects;

public class StringToByteConverter implements FormattedConverter<String, Byte, String> {

    @Override
    public Byte convert(String source, String format) {
        return Byte.valueOf(Objects.requireNonNull(source));
    }
}
