package com.github.dfr.operator;

public interface ParameterValueConverter {

	/**
	 * Convert the value provided as parameter to an expected class
	 * 
	 * @param <R>
	 * @param value
	 * @param expectedClass
	 * @param format
	 * @return The converted value to an expected type. Returns null if parameter
	 *         value is null.
	 */
	<R> R convert(Object value, Class<?> expectedType, Object format);

}
