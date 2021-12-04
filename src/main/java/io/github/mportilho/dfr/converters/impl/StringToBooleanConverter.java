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


import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Converts a {@link String} to a {@link Boolean}
 *
 * @author Marcelo Portilho
 */
public class StringToBooleanConverter extends AbstractCachedStringFormattedConverter<String, Boolean> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean convert(String source, String format) {
        Objects.requireNonNull(source);
        if (!isNullOrBlank(format)
                && cache(format, f -> new HashSet<>(List.of(f.split(",", 1000)))).contains(source)) {
            return Boolean.TRUE;
        }
        return switch (source.toLowerCase()) {
            case "true", "on", "yes", "1" -> Boolean.TRUE;
            default -> Boolean.FALSE;
        };
    }

}
