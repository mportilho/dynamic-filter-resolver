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

package io.github.mportilho.dfr.core.statement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.type.NotEquals;

public class TestConditionalStatement {

	@Test
	public void testNullClauses() {
		ConditionalStatement condition;

		condition = new ConditionalStatement(LogicType.DISJUNCTION, false, null, null);
		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(condition.isConjunction()).isFalse();
		assertThat(condition.isNegate()).isFalse();
		assertThat(condition.getClauses()).isNotNull().isEmpty();
		assertThat(condition.getSubStatements()).isNotNull().isEmpty();

		condition = new ConditionalStatement(LogicType.CONJUNCTION, true, null, null);
		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(condition.isConjunction()).isTrue();
		assertThat(condition.isNegate()).isTrue();
		assertThat(condition.getClauses()).isNotNull().isEmpty();
		assertThat(condition.getSubStatements()).isNotNull().isEmpty();
	}

	@Test
	public void testOneClause() {
		List<FilterParameter> clauses = new ArrayList<>();
		clauses.add(new FilterParameter("name", "name", new String[] { "name" }, String.class, NotEquals.class, false, false,
				new String[] { "Blanka" }, null));
		ConditionalStatement condition = new ConditionalStatement(LogicType.CONJUNCTION, false, clauses, null);

		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(condition.isConjunction()).isTrue();
		assertThat(condition.isNegate()).isFalse();
		assertThat(condition.getClauses()).isNotNull().isNotEmpty().hasSize(1).flatExtracting("values").contains("Blanka");
		assertThat(condition.getSubStatements()).isNotNull().isEmpty();
	}

}
