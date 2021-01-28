package com.github.dfr.provider.specification.converters;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

class DateConverterUtils {

	public static final DateTimeFormatter GENERIC_MONTH_YEAR_FORMATTER;
	public static final DateTimeFormatter GENERIC_DATE_FORMATTER;
	public static final DateTimeFormatter GENERIC_TIME_FORMATTER;
	public static final DateTimeFormatter GENERIC_DATETIME_FORMATTER;

	static {
		String DEF_YEAR_MONTH = "yyyyMM";
		String DEF_SLASH_YEAR_MONTH = "MM/yyyy";
		String DEF_DASH_YEAR_MONTH = "MM-yyyy";

		String DEF_SLASH_DATE = "yyyy/MM[/dd]";
		String DEF_DASH_DATE = "yyyy-MM[-dd]";
		String DEF_SLASH_DATE_PTBR = "dd/MM/yyyy";
		String DEF_DASH_DATE_PTBR = "dd-MM-yyyy";

		String DEF_DATE_TIME_SEPARATOR = "['Z']['T']['z']['t'][ ][-]";

		String DEF_TIME = "HH:mm[:ss[.SSS]]";
		String DEF_TIMEZONE = "[ Z][Z][XXX]['Z']";

		StringBuilder sb;

		sb = new StringBuilder();
		appendOptional(sb, DEF_YEAR_MONTH, DEF_SLASH_YEAR_MONTH, DEF_DASH_YEAR_MONTH);
		GENERIC_MONTH_YEAR_FORMATTER = new DateTimeFormatterBuilder().appendPattern(sb.toString()).toFormatter();

		sb = new StringBuilder();
		appendOptional(sb, DEF_SLASH_DATE, DEF_DASH_DATE, DEF_SLASH_DATE_PTBR, DEF_DASH_DATE_PTBR);
		GENERIC_DATE_FORMATTER = new DateTimeFormatterBuilder().append(DateTimeFormatter.BASIC_ISO_DATE).appendPattern(sb.toString()).toFormatter();

		sb = new StringBuilder();
		appendOptional(sb, String.format("%s[%s]", DEF_TIME, DEF_TIMEZONE));
		GENERIC_TIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern(sb.toString()).toFormatter();

		sb = new StringBuilder();
		appendOptional(sb, String.format("%s[%s%s][%s]", DEF_SLASH_DATE, DEF_DATE_TIME_SEPARATOR, DEF_TIME, DEF_TIMEZONE));
		appendOptional(sb, String.format("%s[%s%s][%s]", DEF_DASH_DATE, DEF_DATE_TIME_SEPARATOR, DEF_TIME, DEF_TIMEZONE));
		appendOptional(sb, String.format("%s[%s%s][%s]", DEF_SLASH_DATE_PTBR, DEF_DATE_TIME_SEPARATOR, DEF_TIME, DEF_TIMEZONE));
		appendOptional(sb, String.format("%s[%s%s][%s]", DEF_DASH_DATE_PTBR, DEF_DATE_TIME_SEPARATOR, DEF_TIME, DEF_TIMEZONE));
		GENERIC_DATETIME_FORMATTER = new DateTimeFormatterBuilder().append(DateTimeFormatter.BASIC_ISO_DATE).appendPattern(sb.toString())
				.toFormatter();
	}

	private static final void appendOptional(StringBuilder sb, String... values) {
		for (String value : values) {
			sb.append("[").append(value).append("]");
		}
	}
}
