package net.dfr.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.dfr.filter.ConditionalStatement;
import net.dfr.filter.DynamicFilterResolver;
import net.dfr.filter.FilterParameter;
import net.dfr.filter.LogicType;
import net.dfr.operator.type.NotEquals;

public class TestAbstractDynamicFilterResolver {

	@Test
	public void testNullParameters() {
		DynamicFilterResolver<List<String>> resolver = new GenericDynamicFilterResolver();
		ConditionalStatement condition = new ConditionalStatement(LogicType.CONJUNCTION, false, null, null);

		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(condition.getClauses()).isNotNull().isEmpty();
		assertThat(condition.getSubStatements()).isNotNull().isEmpty();

		List<String> list = resolver.convertTo(condition);
		assertThat(list).isEmpty();
	}

	@Test
	public void testNoClause() {
		DynamicFilterResolver<List<String>> resolver = new GenericDynamicFilterResolver();
		List<FilterParameter> clauses = new ArrayList<>();
		ConditionalStatement condition = new ConditionalStatement(LogicType.CONJUNCTION, false, clauses, null);

		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(condition.getClauses()).isNotNull().isEmpty();
		assertThat(condition.getSubStatements()).isNotNull().isEmpty();

		List<String> list = resolver.convertTo(condition);
		assertThat(list).isEmpty();
	}

	@Test
	public void testOneClause() {
		DynamicFilterResolver<List<String>> resolver = new GenericDynamicFilterResolver();
		List<FilterParameter> clauses = new ArrayList<>();
		clauses.add(new FilterParameter("name", new String[] { "name" }, String.class, NotEquals.class, false, new String[] { "Blanka" }, null));
		ConditionalStatement condition = new ConditionalStatement(LogicType.CONJUNCTION, false, clauses, null);

		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(condition.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(condition.getSubStatements()).isNotNull().isEmpty();

		List<String> list = resolver.convertTo(condition);
		assertThat(list).isNotEmpty().hasSize(1);
	}

	@Test
	public void testTwoClauses() {
		DynamicFilterResolver<List<String>> resolver = new GenericDynamicFilterResolver();
		List<FilterParameter> clauses = new ArrayList<>();
		clauses.add(new FilterParameter("name", new String[] { "name" }, String.class, NotEquals.class, false, new String[] { "Blanka" }, null));
		clauses.add(new FilterParameter("title", new String[] { "title" }, String.class, NotEquals.class, false, new String[] { "fighter" }, null));
		ConditionalStatement condition = new ConditionalStatement(LogicType.CONJUNCTION, false, clauses, null);

		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(condition.getClauses()).isNotNull().isNotEmpty().hasSize(2);
		assertThat(condition.getSubStatements()).isNotNull().isEmpty();

		List<String> list = resolver.convertTo(condition);
		assertThat(list).isNotEmpty().hasSize(2).containsExactly("Blanka", "fighter");
	}

	@Test
	public void testTwoClausesAndTwoSubConditions() {
		DynamicFilterResolver<List<String>> resolver = new GenericDynamicFilterResolver();

		List<FilterParameter> subClauses1 = new ArrayList<>();
		subClauses1.add(new FilterParameter("name", new String[] { "name" }, String.class, NotEquals.class, false, new String[] { "foo" }, null));
		subClauses1.add(new FilterParameter("title", new String[] { "title" }, String.class, NotEquals.class, false, new String[] { "bah" }, null));
		ConditionalStatement subCondition1 = new ConditionalStatement(LogicType.DISJUNCTION, false, subClauses1, null);

		List<FilterParameter> subClauses2 = new ArrayList<>();
		subClauses2.add(new FilterParameter("weight", new String[] { "weight" }, String.class, NotEquals.class, false, new String[] { "80" }, null));
		ConditionalStatement subCondition2 = new ConditionalStatement(LogicType.DISJUNCTION, false, subClauses2, null);

		List<FilterParameter> clauses = new ArrayList<>();
		clauses.add(new FilterParameter("height", new String[] { "height" }, String.class, NotEquals.class, false, new String[] { "170" }, null));
		clauses.add(new FilterParameter("age", new String[] { "age" }, String.class, NotEquals.class, false, new String[] { "22" }, null));
		ConditionalStatement condition = new ConditionalStatement(LogicType.CONJUNCTION, false, clauses, Arrays.asList(subCondition1, subCondition2));

		assertThat(condition.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(condition.getClauses()).isNotNull().isNotEmpty().hasSize(2);
		assertThat(condition.getSubStatements()).isNotNull().isNotEmpty().hasSize(2);

		List<String> list = resolver.convertTo(condition);
		assertThat(list).isNotEmpty().hasSize(5).containsExactlyInAnyOrder("foo", "bah", "80", "170", "22");
	}

}
