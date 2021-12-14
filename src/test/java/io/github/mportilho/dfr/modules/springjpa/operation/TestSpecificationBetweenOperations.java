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
import io.github.mportilho.dfr.converters.FormattedConversionService;
import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.type.Between;
import io.github.mportilho.dfr.modules.springjpa.samples.application.model.Person;
import org.junit.jupiter.api.BeforeAll;
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
public class TestSpecificationBetweenOperations {

    private static FormattedConversionService formattedConversionService;

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

    @BeforeAll
    public static void setup() {
        formattedConversionService = new DefaultFormattedConversionService();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBetweenOperation_WithOneValue_ThrowingException() {
        SpecBetween<Person> specOp = new SpecBetween<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Between.class, false, false, List.<Object[]>of(new String[]{"TestValue"}), "",
                Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, formattedConversionService);
        assertThatThrownBy(() -> specification.toPredicate(root, query, builder)).isInstanceOf(ArrayIndexOutOfBoundsException.class);

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

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Between.class, false, false, List.<Object[]>of(new String[]{"1", "2", "3"}), "",
                Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, formattedConversionService);
        assertThatThrownBy(() -> specification.toPredicate(root, query, builder)).isInstanceOf(IllegalStateException.class);

        verify(builder, times(0)).between(any(Expression.class), anyString(), anyString());
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testBetweenOperation_WithTwoNullValues() {
        SpecBetween<Person> specOp = new SpecBetween<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Between.class, false, false, List.of(new String[]{null}, new String[]{null}), "",
                Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, formattedConversionService);
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).between((Path) any(), (String) eq(null), (String) eq(null));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testBetweenOperation_WithTwoString() {
        SpecBetween<Person> specOp = new SpecBetween<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Between.class, false, false, List.of(new String[]{"a"}, new String[]{"c"}), "",
                Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, formattedConversionService);
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).between((Expression) any(), eq("a"), eq("c"));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testBetweenOperation_WithTwoString_IgnoringCase() {
        SpecBetween<Person> specOp = new SpecBetween<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(String.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Between.class, false, true, List.of(new String[]{"a"}, new String[]{"c"}), "",
                Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, formattedConversionService);
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).between((Expression) any(), eq("A"), eq("C"));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testBetweenOperation_WithTwoNumbers() {
        SpecBetween<Person> specOp = new SpecBetween<>();

        when(root.getJavaType()).thenReturn(Person.class);
        when(root.get(anyString())).thenReturn(path);
        when(path.getJavaType()).thenReturn(BigDecimal.class);
        when(builder.upper(any())).thenReturn(path);

        FilterData filterData = new FilterData("name", "name", new String[]{"name"}, String.class,
                Between.class, false, true,
                List.of(new BigDecimal[]{BigDecimal.ZERO}, new BigDecimal[]{BigDecimal.ONE}),
                "", Map.of());

        Specification<Person> specification = specOp.createFilter(filterData, formattedConversionService);
        specification.toPredicate(root, query, builder);

        verify(builder, times(1)).between((Expression) any(), eq(BigDecimal.ZERO), eq(new BigDecimal("1")));
    }

}
