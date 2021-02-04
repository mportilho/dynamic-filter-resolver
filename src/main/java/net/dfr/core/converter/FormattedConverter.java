package net.dfr.core.converter;

public interface FormattedConverter<S, T, F> {

	/**
	 * Convert the source object of type {@code S} to target type {@code T}.
	 * 
	 * @param source the source object to convert, which must be an instance of
	 *               {@code S} (never {@code null})
	 * @param format nullable optional format to be used for conversion
	 * @return the converted object, which must be an instance of {@code T}
	 *         (potentially {@code null})
	 * @throws IllegalArgumentException if the source cannot be converted to the
	 *                                  desired target type
	 */
	T convert(S source, F format);

}
