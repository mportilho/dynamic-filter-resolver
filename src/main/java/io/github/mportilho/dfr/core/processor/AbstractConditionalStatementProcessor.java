package io.github.mportilho.dfr.core.processor;

import io.github.mportilho.dfr.core.operation.FilterData;

import java.util.*;

public abstract class AbstractConditionalStatementProcessor<T> implements ConditionalStatementProcessor<T> {

    protected final ValueExpressionResolver<?> valueExpressionResolver;

    protected AbstractConditionalStatementProcessor() {
        this.valueExpressionResolver = v -> null;
    }

    protected AbstractConditionalStatementProcessor(ValueExpressionResolver<?> valueExpressionResolver) {
        this.valueExpressionResolver = valueExpressionResolver != null ? valueExpressionResolver : v -> null;
    }

    /**
     *
     */
    protected List<Object[]> computeValues(String[] parameters, Object[] defaultValues, Map<String, Object[]> parametersMap) {
        if (parameters == null || parameters.length == 0) {
            Object[] value = computeValue(null, defaultValues, parametersMap);
            return value != null ? List.<Object[]>of(value) : Collections.emptyList();
        }

        List<Object[]> valueList = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Object defaultValue = defaultValues != null && defaultValues.length > 0 ? defaultValues[i] : null;
            Object[] value = computeValue(parameters[i], defaultValue, parametersMap);
            if (value != null) {
                valueList.add(value);
            }
        }
        return valueList;
    }

    /**
     *
     */
    protected Object[] computeValue(String parameter, Object defaultValue, Map<String, Object[]> parametersMap) {
        if (parameter != null && !parameter.isBlank() && parametersMap.containsKey(parameter)) {
            return parametersMap.get(parameter);
        } else {
            if (defaultValue instanceof Object[] arr) {
                return Arrays.stream(arr)
                        .mapMulti((obj, mapper) -> {
                            if (obj instanceof String str) {
                                Object resolvedValue = applyExpressionResolver(str);
                                mapper.accept(resolvedValue != null ? resolvedValue : str);
                            } else {
                                mapper.accept(obj);
                            }
                        }).toArray();
            } else if (defaultValue instanceof String strValue && !strValue.isBlank()) {
                Object resolvedValue = applyExpressionResolver(strValue);
                return resolvedValue != null ? new Object[]{resolvedValue} : new Object[]{strValue};
            }
            return null;
        }
    }

    private Object applyExpressionResolver(String value) {
        try {
            return valueExpressionResolver.resolveValue(value);
        } catch (Exception e) {
            throw new IllegalStateException("Provided expression resolver threw an error", e);
        }
    }

    @Override
    public FilterData decorateFilterData(FilterData filterData, Map<String, Object[]> parametersMap) {
        return filterData;
    }

}
