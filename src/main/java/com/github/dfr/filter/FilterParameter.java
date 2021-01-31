package com.github.dfr.filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.github.dfr.operator.FilterOperator;

public class FilterParameter {

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
	 * The provided parameter values from the caller
	 */
	private final Object[] values;

	/**
	 * Optional format pattern to assist data conversion for each parameter. It's
	 * recommended that each parameter has its own provided format for configuration
	 * clarity
	 */
	private final String[] formats;

	/**
	 * The parameter's mutable state during filter resolution
	 */
	private Map<Object, Object> state = new HashMap<>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterParameter(String path, String[] parameters, Class<?> targetType, Class<? extends FilterOperator> operator, boolean negate,
			Object[] value, String[] formats) {
		this.path = path;
		this.parameters = parameters;
		this.targetType = targetType;
		this.operator = (Class<? extends FilterOperator<?>>) operator;
		this.negate = negate;
		this.values = value;
		this.formats = formats;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterParameter(String path, String parameter, Class<?> targetType, Class<? extends FilterOperator> operator, boolean negate, Object value,
			String format) {
		this.path = path;
		this.parameters = new String[] { parameter };
		this.targetType = targetType;
		this.operator = (Class<? extends FilterOperator<?>>) operator;
		this.negate = negate;
		this.values = new Object[] { value };
		this.formats = new String[] { format };
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

	public Object[] getValues() {
		return values;
	}

	public String[] getParameters() {
		return parameters;
	}

	public Class<? extends FilterOperator<?>> getOperator() {
		return operator;
	}

	public String[] getFormats() {
		return formats;
	}

	public void addState(Object key, Object value) {
		state.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <V> V findState(Object key) {
		return (V) state.get(key);
	}

	@SuppressWarnings("unchecked")
	public <V> V findStateOrDefault(Object key, V defaultValue) {
		V stateValue = (V) state.get(key);
		return stateValue != null ? stateValue : defaultValue;
	}

	public Object findValue() {
		if (values == null || values.length == 0) {
			return null;
		} else if (values.length > 1) {
			throw new IllegalStateException("Cannot get single value because multiple values are present");
		}
		return values[0];
	}

	public String findFormat() {
		if (formats == null || formats.length == 0) {
			return null;
		} else if (formats.length > 1) {
			throw new IllegalStateException("Cannot get single value because multiple values are present");
		}
		return formats[0];
	}

	@Override
	public String toString() {
		return "FilterParameter [path=" + path + ", parameters=" + Arrays.toString(parameters) + ", targetType=" + targetType.getSimpleName()
				+ ", operator=" + operator.getSimpleName() + ", negate=" + negate + ", values=" + Arrays.toString(values) + ", formats="
				+ Arrays.toString(formats) + "]";
	}

}
