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
 * @param <T>
 */
public interface DynamicFilterResolver<T> {

	/**
	 * Converts the conditional statement to a target framework's query structure
	 * object
	 * 
	 * @param <R>
	 * @param <K>
	 * @param <V>
	 * @param conditionalStatement the conditional statement to be converted
	 * @param context              an optional context map containing additional
	 *                             data for assisting the conversion
	 * @return
	 */
	<R extends T, K, V> R convertTo(ConditionalStatement conditionalStatement, Map<K, V> context);

	/**
	 * Converts the conditional statement to a target framework's query structure
	 * object, without additional context
	 * 
	 * @param <R>
	 * @param conditionalStatement
	 * @return
	 */
	default <R extends T> R convertTo(ConditionalStatement conditionalStatement) {
		return convertTo(conditionalStatement, null);
	}

	/**
	 * A method that can be overridden to decorate the resulting converted object
	 * 
	 * @param <R>
	 * @param <K>
	 * @param <V>
	 * @param response
	 * @param context
	 * @return
	 */
	default <R extends T, K, V> R responseDecorator(R response, Map<K, V> context) {
		return response;
	}

}
