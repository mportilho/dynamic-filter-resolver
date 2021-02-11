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

package io.github.mportilho.dfr.core.statement;

import java.lang.annotation.Annotation;
import java.util.Map;

import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.FilterOperator;

/**
 * Defines a set of steps for creating {@link ConditionalStatement}'s
 * representation graph.
 * 
 * <p>
 * Validations of any kind about the required values must not be made here, to
 * provide more flexibility to the {@link FilterOperator} implementations
 * 
 * @author Marcelo Portilho
 *
 */
public interface ConditionalStatementProvider {

	/**
	 * Creates {@link ConditionalStatement} representations from types and
	 * annotations
	 * 
	 * @param <K>                  Map key type
	 * @param <V>                  Map value type
	 * @param parameterInterface   Class from which filter configuration will be
	 *                             extracted
	 * @param parameterAnnotations Annotations from which filter configuration will
	 *                             be extracted
	 * @param parametersMap        Map containing provided values for filter
	 *                             operations
	 * @return Conditional statement representation
	 */
	<K, V> ConditionalStatement createConditionalStatements(Class<?> parameterInterface, Annotation[] parameterAnnotations,
			Map<K, V[]> parametersMap);

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
	 *         a filter parameter for this {@link ConditionalStatementProvider}
	 *         implementation. Normally, parameters a not created if there's no
	 *         default, constant or user's value provided.
	 */
	@SuppressWarnings("rawtypes")
	default boolean operatorAcceptsNullValues(Class<? extends FilterOperator> clazz) {
		return false;
	}

}
