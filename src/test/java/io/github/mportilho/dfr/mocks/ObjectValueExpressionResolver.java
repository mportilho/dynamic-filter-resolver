package io.github.mportilho.dfr.mocks;

import io.github.mportilho.dfr.core.processor.ValueExpressionResolver;

import java.util.Map;

public class ObjectValueExpressionResolver implements ValueExpressionResolver<Object> {

    private final Map<String, Object> valueMap;

    public ObjectValueExpressionResolver(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    @Override
    public Object resolveValue(String value) {
        if (value == null) {
            return null;
        }
        return valueMap.get(value);
    }

}
