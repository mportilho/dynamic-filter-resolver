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

package io.github.mportilho.dfr.modules.springjpa.operation;

import io.github.mportilho.dfr.converters.DefaultFormattedConversionService;
import io.github.mportilho.dfr.core.operation.ComparisonOperation;
import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.type.Dynamic;
import io.github.mportilho.dfr.modules.springjpa.samples.application.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;
import static org.assertj.core.api.Assertions.*;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Dynamic.class, false, true,
                List.<Object[]>of(new Object[]{"TestValue", ComparisonOperation.LK}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).like(any(Expression.class), (String) argThat(x -> x.toString().equals("%TESTVALUE%")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_Like_IgnoresCase_WithComparisonEnum_Operation__InvertedValuesPosition() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Dynamic.class, false, true,
                List.<Object[]>of(new Object[]{"TestValue", ComparisonOperation.LK}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).like(any(Expression.class), (String) argThat(x -> x.toString().equals("%TESTVALUE%")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_Like_IgnoresCase_Operation() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Dynamic.class, false, true,
                List.<Object[]>of(new Object[]{"TestValue", "lk"}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).like(any(Expression.class), (String) argThat(x -> x.toString().equals("%TESTVALUE%")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_Like_IgnoresCase_Operation__InvertedValuesPosition() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Dynamic.class, false, true,
                List.<Object[]>of(new Object[]{"lk", "TestValue"}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).like(any(Expression.class), (String) argThat(x -> x.toString().equals("%TESTVALUE%")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_NotEquals_Operation() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("weight", "weight", new String[]{"weight"}, String.class,
                Dynamic.class, false, false,
                List.<Object[]>of(new Object[]{BigDecimal.ZERO, "ne"}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).notEqual(any(Expression.class), eq("0"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_NotEquals_Operation__InvertedValuesPosition() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("weight", "weight", new String[]{"weight"}, String.class,
                Dynamic.class, false, false,
                List.<Object[]>of(new Object[]{"ne", BigDecimal.ZERO}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).notEqual(any(Expression.class), eq("0"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_Invalid_Operation_Throwing() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("weight", "weight", new String[]{"weight"}, String.class,
                Dynamic.class, false, false,
                List.<Object[]>of(new Object[]{BigDecimal.ZERO, "inv"}),
                "", Map.of());

        assertThatThrownBy(() -> specOp.createFilter(filterData, new DefaultFormattedConversionService()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_NotEnoughValues_Operation_Throwing() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("weight", "weight", new String[]{"weight"}, String.class,
                Dynamic.class, false, false,
                List.<Object[]>of(new Object[]{BigDecimal.ZERO}),
                "", Map.of());

        assertThatThrownBy(() -> specOp.createFilter(filterData, new DefaultFormattedConversionService()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Must provide only operation and value for dynamic query");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_NullValue_Operation_Throwing() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("weight", "weight", new String[]{"weight"}, String.class,
                Dynamic.class, false, false,
                List.<Object[]>of(new Object[]{null}),
                "", Map.of());

        assertThatThrownBy(() -> specOp.createFilter(filterData, new DefaultFormattedConversionService()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Must provide only operation and value for dynamic query");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_NoValidOperation_Throwing() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("weight", "weight", new String[]{"weight"}, String.class,
                Dynamic.class, false, false,
                List.<Object[]>of(new Object[]{2, 3}),
                "", Map.of());

        assertThatThrownBy(() -> specOp.createFilter(filterData, new DefaultFormattedConversionService()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Must provide only operation and value for dynamic query");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDynamic_NoValidOperation_WithStrings_Throwing() {
        SpecDynamic<Person> specOp = new SpecDynamic<>(new SpecificationFilterOperationService(new DefaultFormattedConversionService()));

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("weight", "weight", new String[]{"weight"}, String.class,
                Dynamic.class, false, false,
                List.<Object[]>of(new Object[]{"2", "lte"}),
                "", Map.of());

        assertThatThrownBy(() -> specOp.createFilter(filterData, new DefaultFormattedConversionService()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Must provide only operation and value for dynamic query");
    }

}
