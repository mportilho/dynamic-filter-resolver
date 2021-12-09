///*MIT License
//
//Copyright (c) 2021 Marcelo Portilho
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.*/
//
//package io.github.mportilho.dfr.core.resolver;
//
//import io.github.mportilho.dfr.core.operation.FilterData;
//import io.github.mportilho.dfr.core.operation.type.NotEquals;
//import io.github.mportilho.dfr.core.processor.ConditionalStatement;
//import io.github.mportilho.dfr.core.processor.LogicType;
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class TestAbstractDynamicFilterResolver {
//
//    @Test
//    public void testNullParameters() {
//        DynamicFilterResolver<List<?>> resolver = new GenericDynamicFilterResolver();
//        List<String> list = resolver.convertTo(null, null);
//        assertThat(list).isEmpty();
//    }
//
//    @Test
//    public void testNoClause() {
//        DynamicFilterResolver<List<?>> resolver = new GenericDynamicFilterResolver();
//        List<FilterData> clauses = new ArrayList<>();
//        ConditionalStatement condition = new ConditionalStatement("", LogicType.CONJUNCTION, false, clauses, Collections.emptyList());
//
//        assertThat(condition.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
//        assertThat(condition.clauses()).isNotNull().isEmpty();
//        assertThat(condition.oppositeStatements()).isEmpty();
//
//        List<String> list = resolver.convertTo(condition, null);
//        assertThat(list).isEmpty();
//    }
//
//    @Test
//    public void testOneClause() {
//        DynamicFilterResolver<List<?>> resolver = new GenericDynamicFilterResolver();
//        List<FilterData> clauses = new ArrayList<>();
//        clauses.add(new FilterData("name", "name", new String[]{"name"}, String.class, NotEquals.class, false, false,
//                new String[]{"Blanka"}, null, null));
//        ConditionalStatement condition = new ConditionalStatement("", LogicType.CONJUNCTION, false, clauses, Collections.emptyList());
//
//        assertThat(condition.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
//        assertThat(condition.clauses()).isNotNull().isNotEmpty().hasSize(1);
//        assertThat(condition.oppositeStatements()).isEmpty();
//
//        List<String> list = resolver.convertTo(condition, null);
//        assertThat(list).isNotEmpty().hasSize(1);
//    }
//
//    @Test
//    public void testTwoClauses() {
//        DynamicFilterResolver<List<?>> resolver = new GenericDynamicFilterResolver();
//        List<FilterData> clauses = new ArrayList<>();
//        clauses.add(new FilterData("name", "name", new String[]{"name"}, String.class, NotEquals.class, false, false,
//                new String[]{"Blanka"}, null, null));
//        clauses.add(new FilterData("title", "title", new String[]{"title"}, String.class, NotEquals.class, false, false,
//                new String[]{"fighter"}, null, null));
//        ConditionalStatement condition = new ConditionalStatement("", LogicType.CONJUNCTION, false, clauses, Collections.emptyList());
//
//        assertThat(condition.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
//        assertThat(condition.clauses()).isNotNull().isNotEmpty().hasSize(2);
//        assertThat(condition.oppositeStatements()).isEmpty();
//
//        List<String> list = resolver.convertTo(condition, null);
//        assertThat(list).isNotEmpty().hasSize(2).containsExactly("Blanka", "fighter");
//    }
//
//    @Test
//    public void testTwoClausesAndTwoSubConditions() {
//        DynamicFilterResolver<List<?>> resolver = new GenericDynamicFilterResolver();
//
//        List<FilterData> subClauses1 = new ArrayList<>();
//        subClauses1.add(new FilterData("name", "name", new String[]{"name"}, String.class, NotEquals.class, false, false,
//                new String[]{"foo"}, null, null));
//        subClauses1.add(new FilterData("title", "title", new String[]{"title"}, String.class, NotEquals.class, false, false,
//                new String[]{"bah"}, null, null));
//        ConditionalStatement subCondition1 = new ConditionalStatement("", LogicType.DISJUNCTION, false, subClauses1, Collections.emptyList());
//
//        List<FilterData> subClauses2 = new ArrayList<>();
//        subClauses2.add(new FilterData("weight", "weight", new String[]{"weight"}, String.class, NotEquals.class, false, false,
//                new String[]{"80"}, null, null));
//        ConditionalStatement subCondition2 = new ConditionalStatement("", LogicType.DISJUNCTION, false, subClauses2, Collections.emptyList());
//
//        List<FilterData> clauses = new ArrayList<>();
//        clauses.add(new FilterData("height", "height", new String[]{"height"}, String.class, NotEquals.class, false, false,
//                new String[]{"170"}, null, null));
//        clauses.add(
//                new FilterData("age", "age", new String[]{"age"}, String.class, NotEquals.class, false, false,
//                        new String[]{"22"}, null, null));
//        ConditionalStatement condition = new ConditionalStatement("", LogicType.CONJUNCTION, false, clauses, Arrays.asList(subCondition1, subCondition2));
//
//        assertThat(condition.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
//        assertThat(condition.clauses()).isNotNull().isNotEmpty().hasSize(2);
//        assertThat(condition.oppositeStatements()).isNotNull().isNotEmpty().hasSize(2);
//
//        List<String> list = resolver.convertTo(condition, null);
//        assertThat(list).isNotEmpty().hasSize(5).containsExactlyInAnyOrder("foo", "bah", "80", "170", "22");
//    }
//
//}
