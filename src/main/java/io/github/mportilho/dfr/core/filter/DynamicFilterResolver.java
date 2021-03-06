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

import java.util.Map;

import io.github.mportilho.dfr.core.statement.ConditionalStatement;

/**
 * Represents the operation for converting a {@link ConditionalStatement}, with
 * the provided parameters for filtering data, to an input for a specific query
 * framework, like Spring Data JPA's Specification object.
 * 
 * @author Marcelo Portilho
 *
 * @param <T> Return type of the target query object for this dynamic filter
 */
public interface DynamicFilterResolver<T> {

	/**
	 * Converts the conditional statement to a target framework's query structure
	 * object
	 * 
	 * @param <R>                  Return type of the target query object for this
	 *                             dynamic filter
	 * @param <K>                  Map key type
	 * @param <V>                  Map value type
	 * @param conditionalStatement Conditional statement representation for
	 *                             conversion
	 * @param context              Context map containing helping data for statement
	 *                             conversion
	 * @return The query object created from this dynamic filter resolver
	 */
	<R extends T, K, V> R convertTo(ConditionalStatement conditionalStatement, Map<K, V> context);

	/**
	 * Converts the conditional statement to a target framework's query structure
	 * object, without additional context
	 * 
	 * @param <R>                  Return type
	 * @param conditionalStatement Conditional statement representation for
	 *                             conversion
	 * @return The query object created from this dynamic filter resolver
	 */
	default <R extends T> R convertTo(ConditionalStatement conditionalStatement) {
		return convertTo(conditionalStatement, null);
	}

	/**
	 * A method that can be overridden to decorate the resulting converted object
	 * 
	 * @param <R>      Return type of the target query object for this dynamic
	 *                 filter
	 * @param <K>      Map key type
	 * @param <V>      Map value type
	 * @param response The resulting query object created from this dynamic filter
	 * @param context  Context map containing helping data for statement conversion
	 * @return The decorated query object
	 */
	default <R extends T, K, V> R responseDecorator(R response, Map<K, V> context) {
		return response;
	}

}
