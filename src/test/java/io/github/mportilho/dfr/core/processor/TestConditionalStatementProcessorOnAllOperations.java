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

package io.github.mportilho.dfr.core.processor;

import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.type.*;
import io.github.mportilho.dfr.mocks.interfaces.queries.ComparisonOperations;
import io.github.mportilho.dfr.mocks.interfaces.queries.OtherComparisonOperations;
import io.github.mportilho.dfr.mocks.interfaces.queries.StringComparisonOperations;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

public class TestConditionalStatementProcessorOnAllOperations {

//    @Test
//    public void testComparisonOperations() {
//        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor();
//        ConditionalStatement statement = provider.createStatements(new ReflectionParameter(ComparisonOperations.class, null));
//        FilterData param;
//
//        assertThat(statement).isNotNull();
//        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
//        assertThat(statement.negate()).isFalse();
//        assertThat(statement.clauses()).isNotNull().isNotEmpty().hasSize(7);
//        assertThat(statement.oppositeStatements()).isEmpty();
//
//        param = statement.clauses().stream().filter(p -> Between.class.equals(p.operation())).findAny().orElse(null);
//        assertThat(param).isNotNull();
//        assertThat(param.parameters()).isNotEmpty().hasSize(2).containsExactlyInAnyOrder("startDate", "endDate");
//        assertThat(param.values()).isNotEmpty().hasSize(2).containsExactlyInAnyOrder(Arrays.asList(
//                new String[]{"01/01/1980"},
//                new String[]{"01/01/2000"}
//        ).toArray());
//    }

//    @Test
//    public void testStringComparisonOperations() {
//        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor();
//        ConditionalStatement statement = provider.createStatements(new ReflectionParameter(StringComparisonOperations.class, null));
//
//        assertThat(statement).isNotNull();
//        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
//        assertThat(statement.negate()).isFalse();
//        assertThat(statement.clauses()).isNotNull().isNotEmpty().hasSize(10);
//        assertThat(statement.oppositeStatements()).isEmpty();
//    }
//
//    @Test
//    public void testOtherComparisonOperations() {
//        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor();
//        ConditionalStatement statement = provider.createStatements(new ReflectionParameter(OtherComparisonOperations.class, null));
//        FilterData param;
//
//        assertThat(statement).isNotNull();
//        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
//        assertThat(statement.negate()).isFalse();
//        assertThat(statement.clauses()).isNotNull().isNotEmpty().hasSize(4);
//        assertThat(statement.oppositeStatements()).isEmpty();
//
//        param = statement.clauses().stream().filter(p -> IsNull.class.equals(p.operation())).findAny().orElseThrow();
//        assertThat(param.values()).isNotEmpty().hasSize(1).contains("true");
//
//        param = statement.clauses().stream().filter(p -> IsNotNull.class.equals(p.operation())).findAny().orElseThrow();
//        assertThat(param.values()).isNotEmpty().hasSize(1).contains("true");
//
//        param = statement.clauses().stream().filter(p -> IsIn.class.equals(p.operation())).findAny().orElseThrow();
//        assertThat(param.values()).isNotEmpty().hasSize(1);
//        assertThat((Object[]) param.values()[0]).isNotEmpty().hasSize(3).containsExactlyInAnyOrder("170", "180", "190");
//
//        param = statement.clauses().stream().filter(p -> IsNotIn.class.equals(p.operation())).findAny().orElseThrow();
//        assertThat(param.values()).isNotEmpty().hasSize(1);
//        assertThat((Object[]) param.values()[0]).isNotEmpty().hasSize(3).containsExactlyInAnyOrder("1010", "1020", "1030");
//    }

}
