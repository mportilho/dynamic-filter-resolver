package io.github.mportilho.dfr.converters.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalToStringConverter extends AbstractCachedStringFormattedConverter<BigDecimal, String> {

    @Override
    public String convert(BigDecimal source, String format) {
        if (isNullOrBlank(format)) {
            return source.toPlainString();
        }
        return new DecimalFormat(format).format(format);
    }

}
