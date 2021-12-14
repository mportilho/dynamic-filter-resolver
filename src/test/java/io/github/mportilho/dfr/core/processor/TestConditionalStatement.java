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
import io.github.mportilho.dfr.core.operation.type.NotEquals;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestConditionalStatement {

    @Test
    public void testLogicTypeEnum() {
        assertThat(LogicType.CONJUNCTION.opposite()).isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(LogicType.DISJUNCTION.opposite()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(LogicType.CONJUNCTION.isConjunction()).isTrue();
        assertThat(LogicType.DISJUNCTION.isConjunction()).isFalse();
    }

    @Test
    public void testNullClauses() {
        assertThatThrownBy(() -> new ConditionalStatement("", LogicType.CONJUNCTION, false, null, Collections.emptyList()))
                .isInstanceOf(NullPointerException.class).hasMessage("Clause list cannot be null");

        assertThatThrownBy(() -> new ConditionalStatement("", LogicType.CONJUNCTION, false, Collections.emptyList(), null))
                .isInstanceOf(NullPointerException.class).hasMessage("Opposite statement list cannot be null");
    }

    @Test
    public void testOneClause() {
        List<FilterData> clauses = new ArrayList<>();
        clauses.add(new FilterData("name", "name", new String[]{"name"}, String.class, NotEquals.class,
                false, false, List.<Object[]>of(new String[]{"Blanka"}), null, null));
        ConditionalStatement condition = new ConditionalStatement("nameQuery", LogicType.CONJUNCTION, false, clauses, Collections.emptyList());

        assertThat(condition.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(condition.isConjunction()).isTrue();
        assertThat(condition.negate()).isFalse();
        assertThat(condition.oppositeStatements()).isEmpty();
        assertThat(condition.clauses()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(condition.findClauseByPath("name"))
                .isNotEmpty()
                .get().extracting("values").asInstanceOf(InstanceOfAssertFactories.list(Object[].class))
                .containsExactly(new String[]{"Blanka"});

    }

}
