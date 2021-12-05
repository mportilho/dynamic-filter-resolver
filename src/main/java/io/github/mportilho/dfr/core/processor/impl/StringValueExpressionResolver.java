package io.github.mportilho.dfr.core.processor.impl;

import io.github.mportilho.dfr.core.processor.ValueExpressionResolver;

import java.util.Collections;
import java.util.Map;

public class StringValueExpressionResolver implements ValueExpressionResolver<String> {

    private final Map<String, String> valueMap;
    private final ValueExpressionResolver<String> delegate;

    public StringValueExpressionResolver(Map<String, String> valueMap, ValueExpressionResolver<String> delegate) {
        this.valueMap = valueMap != null ? valueMap : Collections.emptyMap();
        this.delegate = delegate;
    }

    @Override
    public String resolveValue(String value) {
        if (valueMap.containsKey(value)) {
            return valueMap.get(value);
        }
        return delegate != null ? delegate.resolveValue(value) : value;
    }

}
