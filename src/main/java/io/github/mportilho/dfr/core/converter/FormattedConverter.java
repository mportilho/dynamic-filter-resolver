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

package io.github.mportilho.dfr.core.converter;

/**
 * A converter that converts a value to a target type, optionally using a
 * provided formatter.
 * 
 * <p>
 * The converter must be thread-safe
 * 
 * @author Marcelo Portilho
 *
 * @param <S> The type of the object to be converter
 * @param <T> The target type
 * @param <F> The optional formatter
 */
public interface FormattedConverter<S, T, F> {

	/**
	 * Convert the source object of type {@code S} to target type {@code T}.
	 * 
	 * @param source the source object to convert, which must be an instance of
	 *               {@code S}
	 * @param format nullable optional format to be used for conversion
	 * @return the converted object, which must be an instance of {@code T}
	 *         (potentially {@code null})
	 * @throws IllegalArgumentException if the source cannot be converted to the
	 *                                  desired target type
	 */
	T convert(S source, F format);

}
