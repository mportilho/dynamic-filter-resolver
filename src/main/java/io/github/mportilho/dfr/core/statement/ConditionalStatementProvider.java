package io.github.mportilho.dfr.core.statement;

import java.util.Map;

import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.FilterOperator;
import io.github.mportilho.dfr.core.statement.annontation.AnnotationConditionalStatementProvider;

/**
 * Defines a set of steps for creating {@link ConditionalStatement}'s
 * representation graph.
 * 
 * <p>
 * Validations of any kind about the required values must not be made here, to
 * provide more flexibility to {@link FilterOperator} implementations
 * 
 * @author Marcelo Portilho
 *
 */
public interface ConditionalStatementProvider {

	/**
	 * Decorator for each {@link FilterParameter} instance created for the
	 * statements
	 * 
	 * @param <K>             Map key type
	 * @param <V>             Map value type
	 * @param filterParameter The filter parameter to be decorated
	 * @param parametersMap   Map containing provided values for filter operations
	 * @return The decorated filter parameter
	 */
	default <K, V> FilterParameter decorateFilterParameter(FilterParameter filterParameter, Map<K, V[]> parametersMap) {
		return filterParameter;
	}

	/**
	 * @param clazz A {@link FilterOperator} to be checked for null acceptance
	 * @return An indication if a {@link FilterOperator} accepts null when creating
	 *         a filter parameter for this
	 *         {@link AnnotationConditionalStatementProvider} implementation.
	 *         Normally, parameters a not created if there's no default, constant or
	 *         user's value provided.
	 */
	@SuppressWarnings("rawtypes")
	default boolean operatorAcceptsNullValues(Class<? extends FilterOperator> clazz) {
		return false;
	}
	
}
