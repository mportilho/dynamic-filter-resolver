package com.github.dfr.filter;

import java.util.HashMap;
import java.util.Map;

import com.github.dfr.decoder.FilterDecoder;

public class ParameterFilterMetadata {

	/**
	 * Attribute name of the parameter type
	 */
	private final String name;

	/**
	 * Parameters needed to be supplied by the caller
	 */
	private final String[] parameters;

	/**
	 * Optional path to the attribute of the parameter type. Useful for other
	 * frameworks
	 */
	private final String path;

	/**
	 * Target attribute type for convertion
	 */
	private final Class<?> targetClass;

	/**
	 * Action to be used as filter
	 */
	private final Class<? extends FilterDecoder<?>> decoderClass;

	/**
	 * The provided parameter value from the caller
	 */
	private final Object[] values;

	private Map<Object, Object> state = new HashMap<>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ParameterFilterMetadata(String name, String[] parameters, String path, Class<?> targetClass, Class<? extends FilterDecoder> decoderClass,
			Object[] value) {
		this.name = name;
		this.parameters = parameters;
		this.path = path;
		this.targetClass = targetClass;
		this.decoderClass = (Class<? extends FilterDecoder<?>>) decoderClass;
		this.values = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ParameterFilterMetadata(String name, String[] parameters, String path, Class<?> targetClass, Class<? extends FilterDecoder> decoderClass,
			Object value) {
		this.name = name;
		this.parameters = parameters;
		this.path = path;
		this.targetClass = targetClass;
		this.decoderClass = (Class<? extends FilterDecoder<?>>) decoderClass;
		this.values = new Object[] { value };
	}

	public String getPath() {
		return path;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Object[] getValues() {
		return values;
	}

	public String getName() {
		return name;
	}

	public String[] getParameters() {
		return parameters;
	}

	public Class<? extends FilterDecoder<?>> getDecoderClass() {
		return decoderClass;
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

	public Object findSingleValue() {
		if (values == null) {
			return null;
		} else if (values.length > 1) {
			throw new IllegalStateException("Cannot get single value because multiple values are present");
		}
		return values[0];
	}

}
