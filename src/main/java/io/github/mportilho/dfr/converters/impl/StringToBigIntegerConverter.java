package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.math.BigInteger;
import java.util.Objects;

public class StringToBigIntegerConverter implements FormattedConverter<String, BigInteger, String> {

    @Override
    public BigInteger convert(String source, String format) {
        return new BigInteger(Objects.requireNonNull(source));
    }
}
