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

package io.github.mportilho.dfr.core.operator;

import java.util.function.Function;

import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.FilterParameter;

/**
 * Represents a logic operation to be applied on a {@link Filter}'s clause
 * 
 * @author Marcelo Portilho
 *
 * @param <T> Return type of the query object created from this dynamic filter
 *            resolver
 */
public interface FilterOperator<T> {

	/**
	 * Creates a filtering operation for the provided parameter
	 * 
	 * @param filterParameter      The representation of a query filter
	 * @param filterValueConverter Convert filter values to the target attribute's
	 *                             type
	 * @return The resulting query object created from the {@link FilterParameter}
	 */
	T createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter);

	/**
	 * Helps applying transformation logic on non null values
	 * 
	 * @param <P>      The source object's type
	 * @param <R>      The converted object's type
	 * @param value    The source value to be transformed
	 * @param function The transformation function
	 * @return The transformed object from the provided function, or null if source
	 *         object is null
	 */
	default <P, R> R transformNonNull(P value, Function<P, R> function) {
		if (value != null) {
			return function.apply(value);
		}
		return null;
	}

}
