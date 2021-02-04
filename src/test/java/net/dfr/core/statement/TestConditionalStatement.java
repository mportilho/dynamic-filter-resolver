package net.dfr.core.statement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.type.NotEquals;

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
		clauses.add(
				new FilterParameter("name", "name", new String[] { "name" }, String.class, NotEquals.class, false, new String[] { "Blanka" }, null));
		ConditionalStatement condition = new ConditionalStatement(LogicType.CONJUNCTION, false, clauses, null);

		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(condition.isConjunction()).isTrue();
		assertThat(condition.isNegate()).isFalse();
		assertThat(condition.getClauses()).isNotNull().isNotEmpty().hasSize(1).flatExtracting("values").contains("Blanka");
		assertThat(condition.getSubStatements()).isNotNull().isEmpty();
	}

}
