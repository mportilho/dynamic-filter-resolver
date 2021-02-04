package net.dfr.core.filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.dfr.core.operator.FilterOperator;

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
			boolean negate, Object[] value, String formats) {
		this.attributePath = attributePath;
		this.path = path;
		this.parameters = parameters;
		this.targetType = targetType;
		this.operator = (Class<? extends FilterOperator<?>>) operator;
		this.negate = negate;
		this.values = value;
		this.format = formats;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterParameter(String attributePath, String path, String parameter, Class<?> targetType, Class<? extends FilterOperator> operator,
			boolean negate, Object value, String format) {
		this.attributePath = attributePath;
		this.path = path;
		this.parameters = new String[] { parameter };
		this.targetType = targetType;
		this.operator = (Class<? extends FilterOperator<?>>) operator;
		this.negate = negate;
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

	@Override
	public String toString() {
		return "FilterParameter [attributePath=" + attributePath + ", path=" + path + ", parameters=" + Arrays.toString(parameters) + ", targetType="
				+ targetType.getSimpleName() + ", operator=" + operator.getSimpleName() + ", negate=" + negate + ", values=" + Arrays.toString(values)
				+ ", format=" + format + "]";
	}

}
