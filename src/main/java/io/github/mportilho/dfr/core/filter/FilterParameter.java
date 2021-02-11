/*MIT License

Copyright (c) 2021 Marcelo Portilho

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

package io.github.mportilho.dfr.core.filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.github.mportilho.dfr.core.operator.FilterOperator;

/**
 * It's the presentation of a filter's parameters provided externally,
 * containing the its value, operation to be applied and the entity's path to be
 * used on the query operation
 * 
 * @author Marcelo Portilho
 *
 */
public class FilterParameter {

	/**
	 * <b>Optional:</b> Used to indicate the path to the attribute of the type's
	 * parameter type.
	 * 
	 * <p>
	 * For example: In a controller, the developer can reference to the DTO class
	 * and it's properties with <code>attributePath</code> and indicate the target
	 * JPA entity's attribute with <code>path</code>.
	 */
	private final String attributePath;

	/**
	 * Name or path to the required attribute
	 * 
	 * <p>
	 * <b>Path</b> is the notation which the target attribute can be found from a
	 * specified root attribute, like <code>Person.addresses.streetName</code>
	 */
	private final String path;

	/**
	 * Parameters needed to be supplied by the caller, exposed as input data
	 * requirements
	 */
	private final String[] parameters;

	/**
	 * Target attribute type for convertion
	 */
	private final Class<?> targetType;

	/**
	 * Operation to be used as a query filter
	 */
	private final Class<? extends FilterOperator<?>> operator;

	/**
	 * Negates the logic result of this filter
	 */
	private final boolean negate;

	/**
	 * Indicates that the dynamic filter must try to ignore value's case if its type
	 * is {@link String}
	 */
	private final boolean ignoreCase;

	/**
	 * The provided parameter values from the caller
	 */
	private final Object[] values;

	/**
	 * Optional format pattern to assist data conversion for each parameter. It's
	 * recommended that each parameter has its own provided format for configuration
	 * clarity
	 */
	private final String format;

	/**
	 * The parameter's mutable state during filter resolution
	 */
	private Map<Object, Object> state = new HashMap<>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterParameter(String attributePath, String path, String[] parameters, Class<?> targetType, Class<? extends FilterOperator> operator,
			boolean negate, boolean ignoreCase, Object[] value, String formats) {
		this.attributePath = attributePath;
		this.path = path;
		this.parameters = parameters;
		this.targetType = targetType;
		this.operator = (Class<? extends FilterOperator<?>>) operator;
		this.negate = negate;
		this.ignoreCase = ignoreCase;
		this.values = value;
		this.format = formats;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterParameter(String attributePath, String path, String parameter, Class<?> targetType, Class<? extends FilterOperator> operator,
			boolean negate, boolean ignoreCase, Object value, String format) {
		this.attributePath = attributePath;
		this.path = path;
		this.parameters = new String[] { parameter };
		this.targetType = targetType;
		this.operator = (Class<? extends FilterOperator<?>>) operator;
		this.negate = negate;
		this.ignoreCase = ignoreCase;
		this.values = value != null ? new Object[] { value } : null;
		this.format = format != null && !format.isEmpty() ? format : null;
	}

	public String getAttributePath() {
		return attributePath;
	}

	public String getPath() {
		return path;
	}

	public Class<?> getTargetType() {
		return targetType;
	}

	public boolean isNegate() {
		return negate;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public Object[] getValues() {
		return values;
	}

	public String[] getParameters() {
		return parameters;
	}

	public Class<? extends FilterOperator<?>> getOperator() {
		return operator;
	}

	public String getFormat() {
		return format;
	}

	public void addState(Object key, Object value) {
		state.put(key, value);
	}

	/**
	 * @param <V>
	 * @param key
	 * @return the value of the mutable parameter's state, indexed the be key
	 *         parameter
	 */
	@SuppressWarnings("unchecked")
	public <V> V findState(Object key) {
		return (V) state.get(key);
	}

	/**
	 * 
	 * @param <V>
	 * @param key
	 * @param defaultValue
	 * @return the value of the mutable parameter's state, indexed the be key
	 *         parameter. When no value is found, returns the default parameter's
	 *         value
	 */
	@SuppressWarnings("unchecked")
	public <V> V findStateOrDefault(Object key, V defaultValue) {
		V stateValue = (V) state.get(key);
		return stateValue != null ? stateValue : defaultValue;
	}

	/**
	 * @return the parameter's value if there's one provided from the first array
	 *         position. Returns null if none is found.
	 */
	public Object findValue() {
		if (values == null || values.length == 0) {
			return null;
		}
		return values[0];
	}

	@Override
	public String toString() {
		return "FilterParameter [attributePath=" + attributePath + ", path=" + path + ", parameters=" + Arrays.toString(parameters) + ", targetType="
				+ targetType.getSimpleName() + ", operator=" + operator.getSimpleName() + ", negate=" + negate + ", values=" + Arrays.toString(values)
				+ ", format=" + format + "]";
	}

}
