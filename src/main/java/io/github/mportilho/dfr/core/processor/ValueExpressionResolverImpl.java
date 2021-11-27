package io.github.mportilho.dfr.core.processor;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.Map;

public class ValueExpressionResolverImpl implements ValueExpressionResolver {

    private final Map<String, Object[]> valueMap;
    private final ValueExpressionResolver delegate;

    public ValueExpressionResolverImpl(Map<String, Object[]> valueMap, ValueExpressionResolver delegate) {
        this.valueMap = valueMap != null ? valueMap : Collections.emptyMap();
        this.delegate = delegate;
    }

    @Override
    public String resolveStringValue(String value) {
        Object[] parameterValue = valueMap.getOrDefault(value, null);
        if (parameterValue != null && parameterValue.length > 1) {
            throw new IllegalStateException("Expression language parsable parameter must have only one mapped value");
        } else if (ArrayUtils.isEmpty(parameterValue) && delegate != null) {
            return delegate.resolveStringValue(value);
        }
        return parameterValue != null && parameterValue.length != 0 ? (String) parameterValue[0] : value;
    }

}
