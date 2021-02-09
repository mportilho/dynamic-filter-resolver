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
