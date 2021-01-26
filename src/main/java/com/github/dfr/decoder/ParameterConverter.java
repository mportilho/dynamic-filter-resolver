package com.github.dfr.decoder;

public interface ParameterConverter {

	/**
	 * Convert the value provided as parameter to an expected class
	 * 
	 * @param <R>
	 * @param value
	 * @param expectedClass
	 * @return The converted value to an expected type. Returns null if parameter
	 *         value is null.
	 */
	<R> R convert(Object value, Class<?> expectedType);

}
