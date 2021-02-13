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
import static org.mockito.ArgumentMatchers.argThat;
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
import io.github.mportilho.dfr.core.operator.ComparisonOperator;
import io.github.mportilho.dfr.core.operator.type.EndsWith;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestSpecificationDynamicOperations {

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
	public void testDynamic_Like_IgnoresCase_WithComparisonEnum_Operation() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, EndsWith.class, false, true,
				new Object[] { "TestValue", ComparisonOperator.LK }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).like(any(Expression.class), (String) argThat(x -> x.toString().equals("%TESTVALUE%")));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_Like_IgnoresCase_WithComparisonEnum_Operation__InvertedValuesPosition() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, EndsWith.class, false, true,
				new Object[] { ComparisonOperator.LK, "TestValue" }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).like(any(Expression.class), (String) argThat(x -> x.toString().equals("%TESTVALUE%")));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_Like_IgnoresCase_Operation() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, EndsWith.class, false, true,
				new String[] { "TestValue", "lk" }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).like(any(Expression.class), (String) argThat(x -> x.toString().equals("%TESTVALUE%")));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_Like_IgnoresCase_Operation__InvertedValuesPosition() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("name", "name", new String[] { "name" }, String.class, EndsWith.class, false, true,
				new String[] { "lk", "TestValue" }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).like(any(Expression.class), (String) argThat(x -> x.toString().equals("%TESTVALUE%")));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_Equals_Operation() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("weight", "weight", new String[] { "weight" }, String.class, EndsWith.class, false,
				false, new Object[] { BigDecimal.ZERO, "ne" }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).notEqual(any(Expression.class), eq(BigDecimal.ZERO));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_Equals_Operation__InvertedValuesPosition() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("weight", "weight", new String[] { "weight" }, String.class, EndsWith.class, false,
				false, new Object[] { "ne", BigDecimal.ZERO }, null);

		Specification<Person> specification = specOp.createFilter(filterParameter, new DefaultFilterValueConverter());
		specification.toPredicate(root, query, builder);

		verify(builder, times(1)).notEqual(any(Expression.class), eq(BigDecimal.ZERO));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_Invalid_Operation_Throwing() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("weight", "weight", new String[] { "weight" }, String.class, EndsWith.class, false,
				false, new Object[] { BigDecimal.ZERO, "inv" }, null);

		assertThatThrownBy(() -> specOp.createFilter(filterParameter, new DefaultFilterValueConverter()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_NotEnoughValues_Operation_Throwing() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("weight", "weight", new String[] { "weight" }, String.class, EndsWith.class, false,
				false, new Object[] { BigDecimal.ZERO }, null);

		assertThatThrownBy(() -> specOp.createFilter(filterParameter, new DefaultFilterValueConverter())).isInstanceOf(IllegalArgumentException.class)
				.hasMessageStartingWith("Wrong number of values for dynamic operator");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_NullValue_Operation_Throwing() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("weight", "weight", new String[] { "weight" }, String.class, EndsWith.class, false,
				false, new Object[] { null }, null);

		assertThatThrownBy(() -> specOp.createFilter(filterParameter, new DefaultFilterValueConverter())).isInstanceOf(IllegalArgumentException.class)
				.hasMessageStartingWith("Wrong number of values for dynamic operator");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_NoValidOperation_Throwing() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("weight", "weight", new String[] { "weight" }, String.class, EndsWith.class, false,
				false, new Object[] { 2, 3 }, null);

		assertThatThrownBy(() -> specOp.createFilter(filterParameter, new DefaultFilterValueConverter())).isInstanceOf(IllegalArgumentException.class)
				.hasMessageStartingWith("No valid operation was informed for dynamic comparison");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDynamic_NoValidOperation_WithStrings_Throwing() {
		SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperatorService());

		when(root.getJavaType()).thenReturn(Person.class);
		when(root.get(anyString())).thenReturn(path);
		when(path.getJavaType()).thenReturn(String.class);
		when(builder.upper(any())).thenReturn(path);

		FilterParameter filterParameter = new FilterParameter("weight", "weight", new String[] { "weight" }, String.class, EndsWith.class, false,
				false, new Object[] { "2", "lte" }, null);

		assertThatThrownBy(() -> specOp.createFilter(filterParameter, new DefaultFilterValueConverter())).isInstanceOf(IllegalArgumentException.class)
				.hasMessageStartingWith("No valid operation was informed for dynamic comparison");
	}

}
