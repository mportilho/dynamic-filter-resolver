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

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * Has a set of predefined date formatters to assist the construction of the
 * formatters.
 * 
 * @author Marcelo Portilho
 *
 */
class DateConverterUtils {

	public static final DateTimeFormatter GENERIC_MONTH_YEAR_FORMATTER;
	public static final DateTimeFormatter GENERIC_DATE_FORMATTER;
	public static final DateTimeFormatter GENERIC_TIME_FORMATTER;
	public static final DateTimeFormatter GENERIC_DATETIME_FORMATTER;
	public static final DateTimeFormatter GENERIC_DATETIME_FORMATTER_PADDING_HOURS;

	static {
		String DEF_YEAR_MONTH = "yyyyMM";
		String DEF_SLASH_YEAR_MONTH = "MM/yyyy";
		String DEF_DASH_YEAR_MONTH = "MM-yyyy";
		String DEF_SLASH_YEAR_MONTH_INVERTED = "yyyy/MM";
		String DEF_DASH_YEAR_MONTH_INVERTED = "yyyy-MM";

		String DEF_SLASH_DATE = "yyyy/MM[/dd]";
		String DEF_DASH_DATE = "yyyy-MM[-dd]";
		String DEF_SLASH_DATE_PTBR = "dd/MM/yyyy";
		String DEF_DASH_DATE_PTBR = "dd-MM-yyyy";

		String DEF_DATE_TIME_SEPARATOR = "['Z']['T']['z']['t'][ ][-]";

		String DEF_TIME = "HH:mm[:ss[.SSSSSSSSS]]";
		String DEF_TIMEZONE = "[ Z][Z][XXX]['Z']";

		StringBuilder sb;

		sb = new StringBuilder();
		appendOptional(sb, DEF_YEAR_MONTH, DEF_SLASH_YEAR_MONTH, DEF_DASH_YEAR_MONTH, DEF_SLASH_YEAR_MONTH_INVERTED, DEF_DASH_YEAR_MONTH_INVERTED);
		GENERIC_MONTH_YEAR_FORMATTER = new DateTimeFormatterBuilder().appendPattern(sb.toString()).toFormatter();

		sb = new StringBuilder();
		appendOptional(sb, DEF_SLASH_DATE, DEF_DASH_DATE, DEF_SLASH_DATE_PTBR, DEF_DASH_DATE_PTBR);
		GENERIC_DATE_FORMATTER = new DateTimeFormatterBuilder().appendOptional(DateTimeFormatter.BASIC_ISO_DATE).appendPattern(sb.toString())
				.toFormatter();

		sb = new StringBuilder();
		appendOptional(sb, String.format("%s[%s]", DEF_TIME, DEF_TIMEZONE));
		GENERIC_TIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern(sb.toString()).toFormatter();

		sb = new StringBuilder();
		appendOptional(sb, String.format("%s[%s%s][%s]", DEF_SLASH_DATE, DEF_DATE_TIME_SEPARATOR, DEF_TIME, DEF_TIMEZONE));
		appendOptional(sb, String.format("%s[%s%s][%s]", DEF_DASH_DATE, DEF_DATE_TIME_SEPARATOR, DEF_TIME, DEF_TIMEZONE));
		appendOptional(sb, String.format("%s[%s%s][%s]", DEF_SLASH_DATE_PTBR, DEF_DATE_TIME_SEPARATOR, DEF_TIME, DEF_TIMEZONE));
		appendOptional(sb, String.format("%s[%s%s][%s]", DEF_DASH_DATE_PTBR, DEF_DATE_TIME_SEPARATOR, DEF_TIME, DEF_TIMEZONE));
		GENERIC_DATETIME_FORMATTER = new DateTimeFormatterBuilder().appendOptional(DateTimeFormatter.BASIC_ISO_DATE).appendPattern(sb.toString())
				.toFormatter();

		GENERIC_DATETIME_FORMATTER_PADDING_HOURS = new DateTimeFormatterBuilder().append(GENERIC_DATETIME_FORMATTER)
				.parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).parseDefaulting(ChronoField.NANO_OF_SECOND, 0).toFormatter();
	}

	private static final void appendOptional(StringBuilder sb, String... values) {
		for (String value : values) {
			sb.append("[").append(value).append("]");
		}
	}
}
