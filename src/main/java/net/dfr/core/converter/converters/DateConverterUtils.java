package net.dfr.core.converter.converters;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

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
