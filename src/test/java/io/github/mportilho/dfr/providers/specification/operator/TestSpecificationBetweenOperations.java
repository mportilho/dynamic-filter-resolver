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

package io.github.mportilho.dfr.providers.specification.operator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;

import io.github.mportilho.apps.apptest.domain.model.Person;
import io.github.mportilho.dfr.core.converter.DefaultFilterValueConverter;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.type.Between;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestSpecificationBetweenOperations {

	@Mock
	private CriteriaBuilder builder;

	@Mock
	@SuppressWarnings("rawtypes")
	private CriteriaQuery query;

	@Mock
	@SuppressWarnings("rawtypes")
	private Root root;

	@Mock
	@SuppressWarnings("rawtypes")
	private Path path;

	@Test
	@SuppressWarnings("unchecked")
	public void testBetweenOperation_WithOneValue_ThrowingException() {
		SpecBetween<Person> specOp = new SpecBetween<>();

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, Between.class, false, false,
				new String[] { "TestValue" }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		assertThatThrownBy(() -> specification.toPredicate(root, query, builder)).isInstanceOf(IllegalStateException.class)
				.hasMessageStartingWith("Wrong number of arguments");

		verify(builder, times(0)).between(any(Expression.class), anyString(), anyString());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBetweenOperation_WithMoreThenTwoValues_ThrowingException() {
		SpecBetween<Person> specOp = new SpecBetween<>();

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, Between.class, false, false,
				new String[] { "1", "2", "3" }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		assertThatThrownBy(() -> specification.toPredicate(root, query, builder)).isInstanceOf(IllegalStateException.class)
				.hasMessageStartingWith("Wrong number of arguments");

		verify(builder, times(0)).between(any(Expression.class), anyString(), anyString());
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testBetweenOperation_WithTwoNullValues() {
		SpecBetween<Person> specOp = new SpecBetween<>();

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, Between.class, false, false,
				new String[] { null, null }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).between((Path) any(), (String) eq(null), (String) eq(null));
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testBetweenOperation_WithTwoString() {
		SpecBetween<Person> specOp = new SpecBetween<>();

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, Between.class, false, false,
				new String[] { "a", "c" }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).between((Expression) any(), eq("a"), eq("c"));
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testBetweenOperation_WithTwoString_IgnoringCase() {
		SpecBetween<Person> specOp = new SpecBetween<>();

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, Between.class, false, true,
				new String[] { "a", "c" }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).between((Expression) any(), eq("A"), eq("C"));
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testBetweenOperation_WithTwoNumbers() {
		SpecBetween<Person> specOp = new SpecBetween<>();

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(BigDecimal.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("height", "height", new String[] { "height" }, BigDecimal.class, Between.class, false,
				false, new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ONE }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).between((Expression) any(), eq(BigDecimal.ZERO), eq(new BigDecimal("1")));
	}

}
