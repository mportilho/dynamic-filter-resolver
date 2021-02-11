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

package io.github.mportilho.dfr.core.converter.converters;

import java.util.HashSet;
import java.util.Set;

import io.github.mportilho.dfr.core.converter.FormattedConverter;

/**
 * Converts a {@link String} to a {@link Boolean}
 * 
 * @author Marcelo Portilho
 *
 */
public class StringToBooleanConverter implements FormattedConverter<String, Boolean, String> {

	private static final Set<String> TRUE_VALUES = new HashSet<>(4);
	private static final Set<String> FALSE_VALUES = new HashSet<>(4);

	static {
		TRUE_VALUES.add("true");
		TRUE_VALUES.add("on");
		TRUE_VALUES.add("yes");
		TRUE_VALUES.add("1");

		FALSE_VALUES.add("false");
		FALSE_VALUES.add("off");
		FALSE_VALUES.add("no");
		FALSE_VALUES.add("0");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean convert(String source, String format) {
		if (source == null || source.trim().isEmpty()) {
			return null;
		}
		String value = source.toLowerCase();
		if (TRUE_VALUES.contains(value)) {
			return Boolean.TRUE;
		} else if (FALSE_VALUES.contains(value)) {
			return Boolean.FALSE;
		} else {
			throw new IllegalArgumentException(String.format("Invalid boolean value '%s'", source));
		}
	}

}
