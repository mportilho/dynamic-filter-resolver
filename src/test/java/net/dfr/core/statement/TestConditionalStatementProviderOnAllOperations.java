package net.dfr.core.statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.type.Between;
import net.dfr.core.operator.type.IsIn;
import net.dfr.core.operator.type.IsNotIn;
import net.dfr.core.operator.type.IsNotNull;
import net.dfr.core.operator.type.IsNull;
import net.dfr.core.statement.interfaces.queries.ComparisonOperations;
import net.dfr.core.statement.interfaces.queries.OtherComparisonOperations;
import net.dfr.core.statement.interfaces.queries.StringComparisonOperations;

public class TestConditionalStatementProviderOnAllOperations {

	@Test
	public void testComparisonOperations() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(StringComparisonOperations.class, null, null);

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(10);
		assertThat(statement.getSubStatements()).isEmpty();
	}

	@Test
	public void testOtherComparisonOperations() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
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
