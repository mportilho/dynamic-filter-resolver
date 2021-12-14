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
import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.type.IsNotNull;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestSpecificationIsNotNullOperations {

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
    public void test_IsNotNullOperation_OnString() {
        SpecIsNotNull<Person> specOp = new SpecIsNotNull<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                IsNotNull.class, false, false,
                List.<Object[]>of(new String[]{"true"}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).isNotNull(any(Expression.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_IsNotNullOperation_OnString_IgnoringCase() {
        SpecIsNotNull<Person> specOp = new SpecIsNotNull<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                IsNotNull.class, false, true,
                List.<Object[]>of(new String[]{"false"}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).isNull(any(Expression.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_IsNotNullOperation_OnNumber() {
        SpecIsNotNull<Person> specOp = new SpecIsNotNull<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(BigDecimal.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, BigDecimal.class,
                IsNotNull.class, false, false,
                List.<Object[]>of(new String[]{"true"}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).isNotNull(any(Expression.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_IsNotNullOperation_OnNumber_IngoringCase() {
        SpecIsNotNull<Person> specOp = new SpecIsNotNull<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(BigDecimal.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, BigDecimal.class,
                IsNotNull.class, false, false,
                List.<Object[]>of(new String[]{"false"}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).isNull(any(Expression.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_IsNullOperation_OnNumber_Throwing_NoValues() {
        SpecIsNotNull<Person> specOp = new SpecIsNotNull<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(BigDecimal.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                IsNotNull.class, false, false,
                List.<Object[]>of(new String[]{null}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, new DefaultFormattedConversionService());

        assertThatThrownBy(() -> specification.toPredicate(root, query, builder)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A boolean value must be provided to resolve the 'IsNotNull' operation");
        verify(builder, times(0)).isNull(any(Expression.class));
    }

}
