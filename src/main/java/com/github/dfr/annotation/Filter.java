package com.github.dfr.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import com.github.dfr.operator.FilterOperator;

@Documented
@Retention(RUNTIME)
public @interface Filter {

	/**
	 * Name or path to the required attribute
	 * 
	 * <p>
	 * <b>Path</b> is the notation which the target attribute can be found from a
	 * specified root attribute, like <code>Person.addresses.streetName</code>
	 * 
	 */
	String path();

	/**
	 * Parameters needed to be supplied by the caller, exposed as input data
	 * requirements
	 */
	String[] parameters();

	/**
	 * Target attribute type for convertion
	 */
	Class<?> targetType() default Object.class;

	/**
	 * Operation to be used as a query filter
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends FilterOperator> operator();

	/**
	 * Indicates that the logic of this filter must be negated.
	 * 
	 * <table>
	 * <tr>
	 * <th>Normal Logic</th>
	 * <th>Negated Logic</th>
	 * </tr>
	 * <tr>
	 * <th>A & B</th>
	 * <th>!(A & B)</th>
	 * </tr>
	 * </table>
	 * 
	 * <p>
	 * Can be parsed by the Spring Expression Language
	 */
	String negate() default "false";

	/**
	 * Default values for each parameter, each at the same position as
	 * <code>parameters</code>
	 * 
	 * <p>
	 * Can be parsed by the Spring Expression Language
	 */
	String[] defaultValues() default {};

	/**
	 * Constant values for each parameter. If this field has any value, the
	 * corresponding filter must not be requested from the user, as it will be used
	 * only internally
	 * 
	 * <p>
	 * Can be parsed by the Spring Expression Language
	 */
	String[] constantValues() default {};

	/**
	 * Optional format pattern to assist data conversion for each parameter. It's
	 * recommended that each parameter has its own provided format for configuration
	 * clarity
	 * 
	 * <p>
	 * Can be parsed by the Spring Expression Language
	 */
	String[] formats() default {};

}
