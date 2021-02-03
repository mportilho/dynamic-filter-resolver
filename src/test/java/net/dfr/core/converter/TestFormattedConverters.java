package net.dfr.core.converter;

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
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import net.dfr.core.converter.impl.StringToInstantConverter;
import net.dfr.core.converter.impl.StringToJavaSqlDateConverter;
import net.dfr.core.converter.impl.StringToJavaUtilDateConverter;
import net.dfr.core.converter.impl.StringToLocalDateConverter;
import net.dfr.core.converter.impl.StringToLocalDateTimeConverter;
import net.dfr.core.converter.impl.StringToLocalTimeConverter;
import net.dfr.core.converter.impl.StringToOffsetDateTimeConverter;
import net.dfr.core.converter.impl.StringToOffsetTimeConverter;
import net.dfr.core.converter.impl.StringToTimestampConverter;
import net.dfr.core.converter.impl.StringToYearConverter;
import net.dfr.core.converter.impl.StringToYearMonthConverter;
import net.dfr.core.converter.impl.StringToZonedDateTimeConverter;

public class TestFormattedConverters {

	@Test
	public void testStringToInstantConverter() {
		StringToInstantConverter converter = new StringToInstantConverter();
		Instant value;

		value = converter.convert("03-12-2011T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("03/12/2011T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("2011-12-03T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("2011/12/03T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("2011/12/03T10:15Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:00Z");
	}

	@Test
	public void testStringToJavaSqlDate() {
		StringToJavaSqlDateConverter converter = new StringToJavaSqlDateConverter();
		Date value;

		value = converter.convert("21/12/2014", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("21-12-2014", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("2014/12/21", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("2014-12-21", null);
		assertThat(value).isEqualTo("2014-12-21");
	}

	@Test
	public void testStringToJavaUtilDate() {
		StringToJavaUtilDateConverter converter = new StringToJavaUtilDateConverter();
		java.util.Date value;

		value = converter.convert("21/12/2014", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("21-12-2014", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("2014/12/21", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("2014-12-21", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("2011/12/03T10:15:00+01:00", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:00-02:00");
	}

	@Test
	public void testStringToLocalDate() {
		StringToLocalDateConverter converter = new StringToLocalDateConverter();
		LocalDate value;

		value = converter.convert("21/12/2014", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("21-12-2014", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("2014-12-21", null);
		assertThat(value).isEqualTo("2014-12-21");

		value = converter.convert("2014/12/21", null);
		assertThat(value).isEqualTo("2014-12-21");
	}

	@Test
	public void testStringToLocalDateTime() {
		StringToLocalDateTimeConverter converter = new StringToLocalDateTimeConverter();
		LocalDateTime value;

		value = converter.convert("21/12/2011T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-21T10:15:30");

		value = converter.convert("21-12-2011T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-21T10:15:30");

		value = converter.convert("2011/12/21T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-21T10:15:30");

		value = converter.convert("2011-12-21T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-21T10:15:30");
	}

	@Test
	public void testStringToLocalTime() {
		StringToLocalTimeConverter converter = new StringToLocalTimeConverter();
		LocalTime value;

		value = converter.convert("10:15:30Z", null);
		assertThat(value).isEqualTo("10:15:30");

		value = converter.convert("10:15:30", null);
		assertThat(value).isEqualTo("10:15:30");

		value = converter.convert("10:15", null);
		assertThat(value).isEqualTo("10:15:00");

		value = converter.convert("10:15Z", null);
		assertThat(value).isEqualTo("10:15:00");
	}

	@Test
	public void testStringToOffsetDateTime() {
		StringToOffsetDateTimeConverter converter = new StringToOffsetDateTimeConverter();
		OffsetDateTime value;

		value = converter.convert("03-12-2007T10:15:30+01:00", null);
		assertThat(value).isEqualTo("2007-12-03T10:15:30+01:00");

		value = converter.convert("03/12/2007T10:15:30+01:00", null);
		assertThat(value).isEqualTo("2007-12-03T10:15:30+01:00");

		value = converter.convert("2007/12/03T10:15:30+01:00", null);
		assertThat(value).isEqualTo("2007-12-03T10:15:30+01:00");

		value = converter.convert("2007-12-03T10:15:30+01:00", null);
		assertThat(value).isEqualTo("2007-12-03T10:15:30+01:00");

		value = converter.convert("2007-12-03T10:15+01:00", null);
		assertThat(value).isEqualTo("2007-12-03T10:15:00+01:00");

		value = converter.convert("2007-12-03T10:15:30-01:00", null);
		assertThat(value).isEqualTo("2007-12-03T10:15:30-01:00");
	}

	@Test
	public void testStringToOffsetTime() {
		StringToOffsetTimeConverter converter = new StringToOffsetTimeConverter();
		OffsetTime value;

		value = converter.convert("13:45:00.123456789+02:00", null);
		assertThat(value).isEqualTo("13:45:00.123456789+02:00");

		value = converter.convert("10:15-01:00", null);
		assertThat(value).isEqualTo("10:15-01:00");
	}

	@Test
	public void testStringToTimestamp() {
		StringToTimestampConverter converter = new StringToTimestampConverter();
		Timestamp value;

		value = converter.convert("03-12-2011T10:15:30Z", null);
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 30, 0)));

		value = converter.convert("03/12/2011T10:15:30Z", null);
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 30, 0)));

		value = converter.convert("2011/12/03T10:15:30Z", null);
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 30, 0)));

		value = converter.convert("2011-12-03T10:15:30Z", null);
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 30, 0)));

		value = converter.convert("2011-12-03T10:15:30Z", null);
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 30, 0)));

		value = converter.convert("2011-12-03T10:15:30", null);
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 30, 0)));

		value = converter.convert("2011-12-03T10:15Z", null);
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 0, 0)));

		value = converter.convert("2011-12-03T10:15", null);
		assertThat(value).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2011, 12, 3, 10, 15, 0, 0)));
	}

	@Test
	public void testStringToYear() {
		StringToYearConverter converter = new StringToYearConverter();
		Year value;

		value = converter.convert("2012", null);
		assertThat(value).isEqualByComparingTo(Year.of(2012));
	}

	@Test
	public void testStringToYearMonth() {
		StringToYearMonthConverter converter = new StringToYearMonthConverter();
		YearMonth value;

		value = converter.convert("12/2012", null);
		assertThat(value).isEqualByComparingTo(YearMonth.of(2012, 12));

		value = converter.convert("2012/12", null);
		assertThat(value).isEqualByComparingTo(YearMonth.of(2012, 12));

		value = converter.convert("201203", null);
		assertThat(value).isEqualByComparingTo(YearMonth.of(2012, 3));
	}

	@Test
	public void testStringToZonedDateTime() {
		StringToZonedDateTimeConverter converter = new StringToZonedDateTimeConverter();
		ZonedDateTime value;

		value = converter.convert("03-12-2011T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("03/12/2011T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("2011-12-03T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("2011/12/03T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("2011/12/03T10:15:30Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:30Z");

		value = converter.convert("2011/12/03T10:15Z", null);
		assertThat(value).isEqualTo("2011-12-03T10:15:00Z");
	}

}
