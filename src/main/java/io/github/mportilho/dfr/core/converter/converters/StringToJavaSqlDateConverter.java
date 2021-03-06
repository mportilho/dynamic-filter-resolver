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

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import io.github.mportilho.dfr.core.converter.AbstractCachedFormattedConverter;

/**
 * Converts a {@link Date} from a {@link String}
 * 
 * @author Marcelo Portilho
 *
 */
public class StringToJavaSqlDateConverter extends AbstractCachedFormattedConverter<String, Date, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date convert(String source, String format) {
		if (format == null || format.isEmpty()) {
			LocalDate localDate = DateConverterUtils.GENERIC_DATE_FORMATTER.parse(source, LocalDate::from);
			return Date.valueOf(localDate);
		}
		LocalDate localDate = cache(format, DateTimeFormatter::ofPattern).parse(source, LocalDate::from);
		return Date.valueOf(localDate);
	}

}
