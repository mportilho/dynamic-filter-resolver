package com.github.dfr.provider.specification;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.github.dfr.filter.ConditionalStatement;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.LogicType;
import com.github.dfr.operator.type.Equals;
import com.github.dfr.provider.AnnotationBasedConditionalStatementProvider;
import com.github.dfr.provider.specification.annotation.AnnotationContainerInterface;
import com.github.dfr.provider.specification.annotation.MethodArgumentAnnotations;
import com.github.dfr.provider.specification.interfaces.FullyRequiringConjunctionInterface;
import com.github.dfr.provider.specification.interfaces.FullyRequiringDisjunctionInterface;
import com.github.dfr.provider.specification.interfaces.NoDeleteAndStatusOkSpecification;
import com.github.dfr.provider.specification.interfaces.NoDeleteExtendedStatusOKSpecification;
import com.github.dfr.provider.specification.interfaces.NoDeleteSpecification;
import com.github.dfr.provider.specification.interfaces.RequiringSomeMoreValuesInterface;
import com.github.dfr.provider.specification.interfaces.RequiringSomeValuesAndSomeDefaultsInterface;
import com.github.dfr.provider.specification.interfaces.RequiringSomeValuesInterface;
import com.github.dfr.provider.specification.interfaces.RequiringValuesDefaultDataInterface;
import com.github.dfr.provider.specification.interfaces.RequiringValuesInterface;
import com.github.dfr.provider.specification.interfaces.StatusEnum;

public class TestAnnotationBasedFilterLogicContextProvider {

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameter() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteSpecification.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);

		FilterParameter filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");
	}

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameterWithStringValueResolver() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(str -> str + "1");
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteSpecification.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);

		FilterParameter filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false1");
	}

	@Test
	public void testOneExtendedInterfaceWithTwoDefaultParameters() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteAndStatusOkSpecification.class, null, Collections.emptyMap());
		FilterParameter filterParameter;

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);

		filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");

		filterParameter = statement.getClauses().get(1);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("status");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("status");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(StatusEnum.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("OK");
	}

	@Test
	public void testOneComposedExtendedInterfaceWithOneDefaultParametersEach() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteExtendedStatusOKSpecification.class, null,
				Collections.emptyMap());
		FilterParameter filterParameter;

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses()).isEmpty();

		ConditionalStatement stmt1 = statement.getSubStatements().get(0);
		assertThat(stmt1).isNotNull();
		assertThat(stmt1.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(stmt1.getSubStatements()).isEmpty();
		assertThat(stmt1.getClauses()).isNotEmpty().hasSize(1);

		ConditionalStatement stmt2 = statement.getSubStatements().get(1);
		assertThat(stmt1).isNotNull();
		assertThat(stmt1.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(stmt1.getSubStatements()).isEmpty();
		assertThat(stmt1.getClauses()).isNotEmpty().hasSize(1);

		filterParameter = stmt1.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");

		filterParameter = stmt2.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("status");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("status");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(StatusEnum.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("OK");
	}

	@Test
	public void testAnnotatedInterfaceAndAnnotatedParameter() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		Annotation[] methodArgumentAnnotations = MethodArgumentAnnotations.class.getAnnotations();

		ConditionalStatement statement = provider.createConditionalStatements(AnnotationContainerInterface.class, methodArgumentAnnotations,
				Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses()).isEmpty();

		ConditionalStatement stmt_0_0 = statement.getSubStatements().get(0);
		assertThat(stmt_0_0).isNotNull();
		assertThat(stmt_0_0.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(stmt_0_0.getClauses()).isEmpty();
		assertThat(stmt_0_0.getSubStatements()).isNotEmpty().hasSize(3);

		ConditionalStatement stmt_0_1 = statement.getSubStatements().get(1);
		assertThat(stmt_0_1).isNotNull();
		assertThat(stmt_0_1.getLogicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(stmt_0_1.getClauses()).isNotEmpty().hasSize(2);
		assertThat(stmt_0_1.getSubStatements()).isEmpty();

		ConditionalStatement stmt_0_0_0 = stmt_0_0.getSubStatements().get(0);
		assertThat(stmt_0_0_0).isNotNull();
		assertThat(stmt_0_0_0.getLogicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(stmt_0_0_0.getClauses()).isNotEmpty().hasSize(2);
		assertThat(stmt_0_0_0.getSubStatements()).isEmpty();

		ConditionalStatement stmt_0_0_1 = stmt_0_0.getSubStatements().get(1);
		assertThat(stmt_0_0_1).isNotNull();
		assertThat(stmt_0_0_1.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(stmt_0_0_1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(stmt_0_0_1.getSubStatements()).isEmpty();

		ConditionalStatement stmt_0_0_2 = stmt_0_0.getSubStatements().get(2);
		assertThat(stmt_0_0_2).isNotNull();
		assertThat(stmt_0_0_2.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(stmt_0_0_2.getClauses()).isNotEmpty().hasSize(1);
		assertThat(stmt_0_0_2.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithRequiredValuesProvided() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement;

		statement = provider.createConditionalStatements(RequiringValuesInterface.class, null,
				Collections.singletonMap("delete", new String[] { "true" }));
		assertThat(statement).isNotNull();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getValues()).isNotEmpty().hasSize(1).contains("true");

		statement = provider.createConditionalStatements(RequiringValuesInterface.class, null,
				Collections.singletonMap("delete", new String[] { "false" }));
		assertThat(statement).isNotNull();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getValues()).isNotEmpty().hasSize(1).contains("false");
	}

	@Test
	public void testWithRequiredValuesNotProvided() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringValuesInterface.class, null, Collections.emptyMap());
		assertThat(statement).isNull();
	}

	@Test
	public void testWithSomeRequiredValuesProvided() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeValuesInterface.class, null, Collections.emptyMap());
		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithSomeMoreRequiredValuesProvided() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeMoreValuesInterface.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(1);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("weight");
		assertThat(or1.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithSomeMoreRequiredValuesAndSomeDefaultsProvided() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeValuesAndSomeDefaultsInterface.class, null,
				Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("birthday");
		assertThat(or1.getSubStatements()).isEmpty();

		ConditionalStatement or2 = statement.getSubStatements().get(1);
		assertThat(or2).isNotNull();
		assertThat(or2.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or2.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or2.getClauses().get(0).getPath()).isEqualTo("student");
		assertThat(or2.getSubStatements()).isEmpty();
	}

	@Test
	public void testRequiredValuesWithDefaultDataProvided() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement;

		statement = provider.createConditionalStatements(RequiringValuesDefaultDataInterface.class, null, Collections.emptyMap());
		assertThat(statement).isNotNull();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getValues()).isNotEmpty().hasSize(1).contains("true");

		statement = provider.createConditionalStatements(RequiringValuesInterface.class, null,
				Collections.singletonMap("delete", new String[] { "false" }));
		assertThat(statement).isNotNull();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getValues()).isNotEmpty().hasSize(1).contains("false");
	}

	@Test
	public void testWithConjunctionAnnotationFullyProvided() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringConjunctionInterface.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getClauses().get(1).getPath()).isEqualTo("name");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("status");
		assertThat(or1.getSubStatements()).isEmpty();

		ConditionalStatement or2 = statement.getSubStatements().get(1);
		assertThat(or2).isNotNull();
		assertThat(or2.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(or2.getClauses()).isNotEmpty().hasSize(2);
		assertThat(or2.getClauses().get(0).getPath()).isEqualTo("birthday");
		assertThat(or2.getClauses().get(1).getPath()).isEqualTo("height");
		assertThat(or2.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithDisjunctionAnnotationFullyProvided() {
		AnnotationBasedConditionalStatementProvider provider = new AnnotationBasedConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringDisjunctionInterface.class, null, Collections.emptyMap());
		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getClauses().get(1).getPath()).isEqualTo("name");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("status");
		assertThat(or1.getSubStatements()).isEmpty();

		ConditionalStatement or2 = statement.getSubStatements().get(1);
		assertThat(or2).isNotNull();
		assertThat(or2.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or2.getClauses()).isNotEmpty().hasSize(2);
		assertThat(or2.getClauses().get(0).getPath()).isEqualTo("birthday");
		assertThat(or2.getClauses().get(1).getPath()).isEqualTo("height");
		assertThat(or2.getSubStatements()).isEmpty();
	}

}
