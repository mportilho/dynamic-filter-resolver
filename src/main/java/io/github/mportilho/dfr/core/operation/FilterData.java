package io.github.mportilho.dfr.core.operation;

import java.util.Map;

//TODO include doc

public record FilterData(
        String attributePath,
        String path,
        String[] parameters,
        Class<?> targetType,
        @SuppressWarnings("rawtypes") Class<? extends FilterOperationManager> operation,
        boolean negate,
        boolean ignoreCase,
        Object[] values,
        String format,
        Map<String, String> modifiers
) {

    /**
     * @return the parameter's value if there's one provided from the first array
     * position. Returns null if none is found.
     */
    public Object findOneValue() {
        if (values == null || values.length == 0) {
            return null;
        }
        if (values.length > 1) {
            throw new IllegalStateException("Multiple values found while fetching a single one");
        }
        return values[0];
    }

    public String findModifier(String name) {
        if (modifiers != null) {
            return modifiers.get(name);
        }
        return null;
    }

}
