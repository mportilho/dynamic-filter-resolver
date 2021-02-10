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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.type.Between;
import io.github.mportilho.dfr.core.operator.type.IsIn;
import io.github.mportilho.dfr.core.operator.type.IsNotIn;
import io.github.mportilho.dfr.core.operator.type.IsNotNull;
import io.github.mportilho.dfr.core.operator.type.IsNull;
import io.github.mportilho.dfr.core.statement.interfaces.queries.ComparisonOperations;
import io.github.mportilho.dfr.core.statement.interfaces.queries.OtherComparisonOperations;
import io.github.mportilho.dfr.core.statement.interfaces.queries.StringComparisonOperations;

public class TestConditionalStatementProviderOnAllOperations {

	@Test
	public void testComparisonOperations() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(ComparisonOperations.class, null, null);
		FilterParameter param;

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(7);
		assertThat(statement.getSubStatements()).isEmpty();

		param = statement.getClauses().stream().filter(p -> Between.class.equals(p.getOperator())).findAny().orElse(null);
		assertThat(param).isNotNull();
		assertThat(param.getParameters()).isNotEmpty().hasSize(2).containsExactlyInAnyOrder("startDate", "endDate");
		assertThat(param.getValues()).isNotEmpty().hasSize(2).containsExactlyInAnyOrder("01/01/1980", "01/01/2000");
		assertThatThrownBy(() -> param.findValue()).isInstanceOf(IllegalStateException.class);
	}

	@Test
	public void testStringComparisonOperations() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(StringComparisonOperations.class, null, null);

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(10);
		assertThat(statement.getSubStatements()).isEmpty();
	}

	@Test
	public void testOtherComparisonOperations() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(OtherComparisonOperations.class, null, null);
		FilterParameter param;

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(4);
		assertThat(statement.getSubStatements()).isEmpty();

		param = statement.getClauses().stream().filter(p -> IsNull.class.equals(p.getOperator())).findAny().orElse(null);
		assertThat(param.getValues()).isNotEmpty().hasSize(1).containsNull();

		param = statement.getClauses().stream().filter(p -> IsNotNull.class.equals(p.getOperator())).findAny().orElse(null);
		assertThat(param.getValues()).isNotEmpty().hasSize(1).containsNull();

		param = statement.getClauses().stream().filter(p -> IsIn.class.equals(p.getOperator())).findAny().orElse(null);
		assertThat(param.getValues()).isNotEmpty().hasSize(1);
		assertThat((Object[]) param.getValues()[0]).containsExactlyInAnyOrder("170", "180", "190");

		param = statement.getClauses().stream().filter(p -> IsNotIn.class.equals(p.getOperator())).findAny().orElse(null);
		assertThat(param.getValues()).isNotEmpty().hasSize(1);
		assertThat((Object[]) param.getValues()[0]).containsExactlyInAnyOrder("1010", "1020", "1030");
	}

}
