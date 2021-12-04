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

package io.github.mportilho.dfr.converters.impl;

import io.github.mportilho.dfr.converters.FormattedConverter;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * An abstract implementation of a FormattedConverter that caches the
 * instance responsible for the formatting with it's pattern format as it's key
 *
 * @param <S> The type of the object to be converter
 * @param <T> The target type
 * @author Marcelo Portilho
 */
abstract class AbstractCachedStringFormattedConverter<S, T> implements FormattedConverter<S, T, String> {

    private Map<String, Object> cache;

    /**
     * Caches the instance responsible for formatting an specific pattern
     *
     * @param <U>      The formatter type
     * @param format   The format pattern representation
     * @param supplier The supplier method for the desired format
     * @return The newly created or cached formatter instance
     */
    @SuppressWarnings("unchecked")
    protected <U> U cache(String format, Function<String, U> supplier) {
        if (cache == null) {
            cache = new WeakHashMap<>();
        }
        return (U) cache.computeIfAbsent(format, supplier);
    }

}
