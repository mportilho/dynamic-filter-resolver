package io.github.mportilho.dfr.core.operation;

//TODO include doc
public record FilterData(
        String attributePath,
        String path,
        String[] parameters,
        Class<?> targetType,
        Class<? extends FilterOperation<?>> operator,
        boolean negate,
        boolean ignoreCase,
        Object[] values,
        String format
) {

    /**
     * @return the parameter's value if there's one provided from the first array
     * position. Returns null if none is found.
     */
    public Object findValue() {
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

}
