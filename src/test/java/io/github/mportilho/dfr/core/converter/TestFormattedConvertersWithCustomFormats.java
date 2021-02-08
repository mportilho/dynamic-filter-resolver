package io.github.mportilho.dfr.core.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import io.github.mportilho.dfr.core.converter.converters.StringToInstantConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToJavaSqlDateConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToJavaUtilDateConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToLocalDateConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToLocalDateTimeConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToLocalTimeConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToOffsetDateTimeConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToOffsetTimeConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToTimestampConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToYearConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToYearMonthConverter;
import io.github.mportilho.dfr.core.converter.converters.StringToZonedDateTimeConverter;

public class TestFormattedConvertersWithCustomFormats {

	@Test
	public void testStringToInstantConverter() {
		StringToInstantConverter converter = new StringToInstantConverter();
		Instant value;

		value = converter.convert("03 12 2011 T 10 15 30 Z", "dd MM yyyy 'T' HH mm ss XXX");
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("03 12 2011 T 10 15 30 Z", "dd MM yyyy 'T' HH mm ss XXX");
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("03 12 2011 T 10 15 / 00 Z", "dd MM yyyy 'T' HH mm / ss XXX");
		assertThat(value).isEqualTo("2011-12-03T10:15:00Z");
	}

	@Test
	public void testStringToJavaSqlDate() {
		StringToJavaSqlDateConverter converter = new StringToJavaSqlDateConverter();
		Date value;

		value = converter.convert("21 12 2014", "dd MM yyyy");
		assertThat(value).isEqualTo(Date.valueOf(LocalDate.of(2014, 12, 21)));
	}

	@Test
	public void testStringToJavaUtilDate() {
		StringToJavaUtilDateConverter converter = new StringToJavaUtilDateConverter();
		java.util.Date value;

		value = converter.convert("21 12 2014 13 12 11", "dd MM yyyy HH mm ss");
		assertThat(value).isEqualTo(java.util.Date.from(LocalDateTime.of(2014, 12, 21, 13, 12, 11).atZone(ZoneId.systemDefault()).toInstant()));

		value = converter.convert("21 12 2014 13 12 11 " + ZonedDateTime.now().getOffset().normalized(), "dd MM yyyy HH mm ss XXX");
		assertThat(value).isEqualTo(java.util.Date.from(LocalDateTime.of(2014, 12, 21, 13, 12, 11).atZone(ZoneId.systemDefault()).toInstant()));
	}

	@Test
	public void testStringToLocalDate() {
		StringToLocalDateConverter converter = new StringToLocalDateConverter();
		LocalDate value;

		value = converter.convert(" 21 12  2014", " dd MM  yyyy");
		assertThat(value).isEqualTo("2014-12-21");
	}

	@Test
	public void testStringToLocalDateTime() {
		StringToLocalDateTimeConverter converter = new StringToLocalDateTimeConverter();
		LocalDateTime value;

		value = converter.convert("21 12 2011 T 10 15 30", "dd MM yyyy 'T' HH mm ss");
		assertThat(value).isEqualTo("2011-12-21T10:15:30");
	}

	@Test
	public void testStringToLocalTime() {
		StringToLocalTimeConverter converter = new StringToLocalTimeConverter();
		LocalTime value;

		value = converter.convert("10 15 30", "HH mm ss");
		assertThat(value).isEqualTo("10:15:30");

		value = converter.convert("10 15", "HH mm");
		assertThat(value).isEqualTo("10:15:00");
	}

	@Test
	public void testStringToOffsetDateTime() {
		StringToOffsetDateTimeConverter converter = new StringToOffsetDateTimeConverter();
		OffsetDateTime value;

		value = converter.convert("03 12 2007 T 10 15 30 +01:00", "dd MM yyyy 'T' HH mm ss XXX");
		assertThat(value).isEqualTo("2007-12-03T10:15:30+01:00");
	}

	@Test
	public void testStringToOffsetTime() {
		StringToOffsetTimeConverter converter = new StringToOffsetTimeConverter();
		OffsetTime value;

		value = converter.convert("10 15  -01:00", "HH mm  XXX");
		assertThat(value).isEqualTo("10:15-01:00");
	}

	@Test
	public void testStringToTimestamp() {
		StringToTimestampConverter converter = new StringToTimestampConverter();
		Timestamp value;

		value = converter.convert("03 12 2011 T 10 15 30 Z", "dd MM yyyy 'T' HH mm ss XXX");
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 30, 0)));
	}

	@Test
	public void testStringToYear() {
		StringToYearConverter converter = new StringToYearConverter();
		Year value;

		value = converter.convert("T2012T", "'T'yyyy'T'");
		assertThat(value).isEqualByComparingTo(Year.of(2012));
	}

	@Test
	public void testStringToYearMonth() {
		StringToYearMonthConverter converter = new StringToYearMonthConverter();
		YearMonth value;

		value = converter.convert("12 2012", "MM yyyy");
		assertThat(value).isEqualByComparingTo(YearMonth.of(2012, 12));

		value = converter.convert("2012 12", "yyyy MM");
		assertThat(value).isEqualByComparingTo(YearMonth.of(2012, 12));

		value = converter.convert("032012", "MMyyyy");
		assertThat(value).isEqualByComparingTo(YearMonth.of(2012, 3));
	}

	@Test
	public void testStringToZonedDateTime() {
		StringToZonedDateTimeConverter converter = new StringToZonedDateTimeConverter();
		ZonedDateTime value;

		value = converter.convert("03 12 2011 T 10 15 30 Z", "dd MM yyyy 'T' HH mm ss XXX");
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");
	}

}
