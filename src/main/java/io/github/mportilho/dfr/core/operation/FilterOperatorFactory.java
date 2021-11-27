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

package io.github.mportilho.dfr.core.operation;

/**
 * Contains a set of {@link FilterOperator} implementations for a specific query
 * framework.
 *
 * @param <T> Return type of the query object created from this dynamic filter
 *            resolver
 * @author Marcelo Portilho
 */
public interface FilterOperatorFactory<T> {

    /**
     * @param <R>      The current {@link FilterOperator} type implementation
     * @param operator the {@link FilterOperator} class used to query a specific
     *                 implementation
     * @return the implementation of a requested {@link FilterOperator}
     */
    <R extends FilterOperation<T>> R createFilter(Class<? extends FilterOperation<T>> operation);

}