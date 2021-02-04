package net.dfr.core.statement;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.type.Equals;
import net.dfr.core.statement.annotation.AnnotationContainerInterface;
import net.dfr.core.statement.annotation.MethodArgumentAnnotations;
import net.dfr.core.statement.interfaces.FullyRequiringConjunction;
import net.dfr.core.statement.interfaces.FullyRequiringConjunctionNegatingAll;
import net.dfr.core.statement.interfaces.FullyRequiringDisjunction;
import net.dfr.core.statement.interfaces.FullyRequiringDisjunctionNegatingAll;
import net.dfr.core.statement.interfaces.NoDelete;
import net.dfr.core.statement.interfaces.NoDeleteAndStatusOk;
import net.dfr.core.statement.interfaces.NoDeleteExtendedStatusOK;
import net.dfr.core.statement.interfaces.NoDeleteNegatingPredicate;
import net.dfr.core.statement.interfaces.RequiringSomeMoreValues;
import net.dfr.core.statement.interfaces.RequiringSomeValues;
import net.dfr.core.statement.interfaces.RequiringSomeValuesAndSomeDefaults;
import net.dfr.core.statement.interfaces.RequiringValues;
import net.dfr.core.statement.interfaces.RequiringValuesDefaultData;
import net.dfr.core.statement.interfaces.StatusEnum;

public class TestConditionalStatementProvider {

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameter() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDelete.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);

		FilterParameter filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");
	}

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameterNegatingPredicate() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteNegatingPredicate.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);

		FilterParameter filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isTrue();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");
	}

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameterWithStringValueResolver() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(str -> str + "1");
		ConditionalStatement statement = provider.createConditionalStatements(NoDelete.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);

		FilterParameter filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false1");
	}

	@Test
	public void testOneExtendedInterfaceWithTwoDefaultParameters() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteAndStatusOk.class, null, Collections.emptyMap());
		FilterParameter filterParameter;

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);

		filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");

		filterParameter = statement.getClauses().get(1);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("status");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("status");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(StatusEnum.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("OK");
	}

	@Test
	public void testOneComposedExtendedInterfaceWithOneDefaultParametersEach() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteExtendedStatusOK.class, null, Collections.emptyMap());
		FilterParameter filterParameter;

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses()).isEmpty();

		ConditionalStatement stmt1 = statement.getSubStatements().get(0);
		assertThat(stmt1).isNotNull();
		assertThat(stmt1.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(stmt1.getSubStatements()).isEmpty();
		assertThat(stmt1.getClauses()).isNotEmpty().hasSize(1);

		ConditionalStatement stmt2 = statement.getSubStatements().get(1);
		assertThat(stmt1).isNotNull();
		assertThat(stmt1.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(stmt1.getSubStatements()).isEmpty();
		assertThat(stmt1.getClauses()).isNotEmpty().hasSize(1);

		filterParameter = stmt1.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");

		filterParameter = stmt2.getClauses().get(0);
		assertThat(filterParameter.getFormats()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("status");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("status");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(StatusEnum.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("OK");
	}

	@Test
	public void testAnnotatedInterfaceAndAnnotatedParameter() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		Annotation[] methodArgumentAnnotations = MethodArgumentAnnotations.class.getAnnotations();

		ConditionalStatement statement = provider.createConditionalStatements(AnnotationContainerInterface.class, methodArgumentAnnotations,
				Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses()).isEmpty();

		ConditionalStatement stmt_0_0 = statement.getSubStatements().get(0);
		assertThat(stmt_0_0).isNotNull();
		assertThat(stmt_0_0.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(stmt_0_0.isNegate()).isFalse();
		assertThat(stmt_0_0.getClauses()).isEmpty();
		assertThat(stmt_0_0.getSubStatements()).isNotEmpty().hasSize(3);

		ConditionalStatement stmt_0_1 = statement.getSubStatements().get(1);
		assertThat(stmt_0_1).isNotNull();
		assertThat(stmt_0_1.getLogicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(stmt_0_1.isNegate()).isFalse();
		assertThat(stmt_0_1.getClauses()).isNotEmpty().hasSize(2);
		assertThat(stmt_0_1.getSubStatements()).isEmpty();

		ConditionalStatement stmt_0_0_0 = stmt_0_0.getSubStatements().get(0);
		assertThat(stmt_0_0_0).isNotNull();
		assertThat(stmt_0_0_0.getLogicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(stmt_0_0_0.isNegate()).isFalse();
		assertThat(stmt_0_0_0.getClauses()).isNotEmpty().hasSize(2);
		assertThat(stmt_0_0_0.getSubStatements()).isEmpty();

		ConditionalStatement stmt_0_0_1 = stmt_0_0.getSubStatements().get(1);
		assertThat(stmt_0_0_1).isNotNull();
		assertThat(stmt_0_0_1.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(stmt_0_0_1.isNegate()).isFalse();
		assertThat(stmt_0_0_1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(stmt_0_0_1.getSubStatements()).isEmpty();

		ConditionalStatement stmt_0_0_2 = stmt_0_0.getSubStatements().get(2);
		assertThat(stmt_0_0_2).isNotNull();
		assertThat(stmt_0_0_2.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(stmt_0_0_2.isNegate()).isFalse();
		assertThat(stmt_0_0_2.getClauses()).isNotEmpty().hasSize(1);
		assertThat(stmt_0_0_2.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithRequiredValuesProvided() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement;

		statement = provider.createConditionalStatements(RequiringValues.class, null, Collections.singletonMap("delete", new String[] { "true" }));

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getValues()).isNotEmpty().hasSize(1).contains("true");
		assertThat(statement.getSubStatements()).isEmpty();

		statement = provider.createConditionalStatements(RequiringValues.class, null, Collections.singletonMap("delete", new String[] { "false" }));

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getValues()).isNotEmpty().hasSize(1).contains("false");
		assertThat(statement.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithRequiredValuesNotProvided() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringValues.class, null, Collections.emptyMap());
		assertThat(statement).isNull();
	}

	@Test
	public void testWithSomeRequiredValuesProvided() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeValues.class, null, Collections.emptyMap());
		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithSomeMoreRequiredValuesProvided() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeMoreValues.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(1);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(or1.isNegate()).isFalse();
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("weight");
		assertThat(or1.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithSomeMoreRequiredValuesAndSomeDefaultsProvided() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeValuesAndSomeDefaults.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or1.isNegate()).isFalse();
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("birthday");
		assertThat(or1.getSubStatements()).isEmpty();

		ConditionalStatement or2 = statement.getSubStatements().get(1);
		assertThat(or2).isNotNull();
		assertThat(or2.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or2.isNegate()).isFalse();
		assertThat(or2.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or2.getClauses().get(0).getPath()).isEqualTo("student");
		assertThat(or2.getSubStatements()).isEmpty();
	}

	@Test
	public void testRequiredValuesWithDefaultDataProvided() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement;

		statement = provider.createConditionalStatements(RequiringValuesDefaultData.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getClauses().get(0).getValues()).isNotEmpty().hasSize(1).contains("true");
		assertThat(statement.getSubStatements()).isEmpty();

		statement = provider.createConditionalStatements(RequiringValues.class, null, Collections.singletonMap("delete", new String[] { "false" }));

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotNull().isNotEmpty().hasSize(1);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getClauses().get(0).getValues()).isNotEmpty().hasSize(1).contains("false");
		assertThat(statement.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithConjunctionAnnotationFullyProvided() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringConjunction.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getClauses().get(1).getPath()).isEqualTo("name");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(or1.isNegate()).isFalse();
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("status");
		assertThat(or1.getSubStatements()).isEmpty();

		ConditionalStatement or2 = statement.getSubStatements().get(1);
		assertThat(or2).isNotNull();
		assertThat(or2.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(or2.isNegate()).isFalse();
		assertThat(or2.getClauses()).isNotEmpty().hasSize(2);
		assertThat(or2.getClauses().get(0).getPath()).isEqualTo("birthday");
		assertThat(or2.getClauses().get(1).getPath()).isEqualTo("height");
		assertThat(or2.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithConjunctionAnnotationFullyProvidedNegatingAll() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringConjunctionNegatingAll.class, null,
				Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isTrue();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getClauses().get(1).getPath()).isEqualTo("name");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(or1.isNegate()).isTrue();
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("status");
		assertThat(or1.getSubStatements()).isEmpty();

		ConditionalStatement or2 = statement.getSubStatements().get(1);
		assertThat(or2).isNotNull();
		assertThat(or2.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(or2.isNegate()).isTrue();
		assertThat(or2.getClauses()).isNotEmpty().hasSize(2);
		assertThat(or2.getClauses().get(0).getPath()).isEqualTo("birthday");
		assertThat(or2.getClauses().get(1).getPath()).isEqualTo("height");
		assertThat(or2.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithDisjunctionAnnotationFullyProvided() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringDisjunction.class, null, Collections.emptyMap());
		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getClauses().get(1).getPath()).isEqualTo("name");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or1.isNegate()).isFalse();
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("status");
		assertThat(or1.getSubStatements()).isEmpty();

		ConditionalStatement or2 = statement.getSubStatements().get(1);
		assertThat(or2).isNotNull();
		assertThat(or2.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or2.isNegate()).isFalse();
		assertThat(or2.getClauses()).isNotEmpty().hasSize(2);
		assertThat(or2.getClauses().get(0).getPath()).isEqualTo("birthday");
		assertThat(or2.getClauses().get(1).getPath()).isEqualTo("height");
		assertThat(or2.getSubStatements()).isEmpty();
	}

	@Test
	public void testWithDisjunctionAnnotationFullyProvidedNegatingAll() {
		ConditionalStatementProvider provider = new GenericConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringDisjunctionNegatingAll.class, null,
				Collections.emptyMap());
		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
		assertThat(statement.isNegate()).isTrue();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);
		assertThat(statement.getClauses().get(0).getPath()).isEqualTo("deleted");
		assertThat(statement.getClauses().get(1).getPath()).isEqualTo("name");
		assertThat(statement.getSubStatements()).isNotEmpty().hasSize(2);

		ConditionalStatement or1 = statement.getSubStatements().get(0);
		assertThat(or1).isNotNull();
		assertThat(or1.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or1.isNegate()).isTrue();
		assertThat(or1.getClauses()).isNotEmpty().hasSize(1);
		assertThat(or1.getClauses().get(0).getPath()).isEqualTo("status");
		assertThat(or1.getSubStatements()).isEmpty();

		ConditionalStatement or2 = statement.getSubStatements().get(1);
		assertThat(or2).isNotNull();
		assertThat(or2.getLogicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(or2.isNegate()).isTrue();
		assertThat(or2.getClauses()).isNotEmpty().hasSize(2);
		assertThat(or2.getClauses().get(0).getPath()).isEqualTo("birthday");
		assertThat(or2.getClauses().get(1).getPath()).isEqualTo("height");
		assertThat(or2.getSubStatements()).isEmpty();
	}

}
