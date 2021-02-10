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

import java.lang.annotation.Annotation;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.statement.annotation.AnnotationContainerInterface;
import io.github.mportilho.dfr.core.statement.annotation.MethodArgumentAnnotations;
import io.github.mportilho.dfr.core.statement.interfaces.FullyRequiringConjunction;
import io.github.mportilho.dfr.core.statement.interfaces.FullyRequiringConjunctionNegatingAll;
import io.github.mportilho.dfr.core.statement.interfaces.FullyRequiringDisjunction;
import io.github.mportilho.dfr.core.statement.interfaces.FullyRequiringDisjunctionNegatingAll;
import io.github.mportilho.dfr.core.statement.interfaces.NoDelete;
import io.github.mportilho.dfr.core.statement.interfaces.NoDeleteAndStatusOk;
import io.github.mportilho.dfr.core.statement.interfaces.NoDeleteExtendedStatusOK;
import io.github.mportilho.dfr.core.statement.interfaces.NoDeleteNegatingPredicate;
import io.github.mportilho.dfr.core.statement.interfaces.RequiringSomeMoreValues;
import io.github.mportilho.dfr.core.statement.interfaces.RequiringSomeValues;
import io.github.mportilho.dfr.core.statement.interfaces.RequiringSomeValuesAndSomeDefaults;
import io.github.mportilho.dfr.core.statement.interfaces.RequiringValues;
import io.github.mportilho.dfr.core.statement.interfaces.RequiringValuesDefaultData;
import io.github.mportilho.dfr.core.statement.interfaces.StatusEnum;

public class TestConditionalStatementProvider {

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameter() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDelete.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);

		FilterParameter filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormat()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");
	}

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameterNegatingPredicate() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteNegatingPredicate.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);

		FilterParameter filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormat()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isTrue();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");
	}

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameterWithStringValueResolver() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(str -> str + "1");
		ConditionalStatement statement = provider.createConditionalStatements(NoDelete.class, null, Collections.emptyMap());

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(1);

		FilterParameter filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormat()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false1");
	}

	@Test
	public void testOneExtendedInterfaceWithTwoDefaultParameters() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteAndStatusOk.class, null, Collections.emptyMap());
		FilterParameter filterParameter;

		assertThat(statement).isNotNull();
		assertThat(statement.getLogicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
		assertThat(statement.isNegate()).isFalse();
		assertThat(statement.getSubStatements()).isEmpty();
		assertThat(statement.getClauses()).isNotEmpty().hasSize(2);

		filterParameter = statement.getClauses().get(0);
		assertThat(filterParameter.getFormat()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");

		filterParameter = statement.getClauses().get(1);
		assertThat(filterParameter.getFormat()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("status");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("status");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(StatusEnum.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("OK");
	}

	@Test
	public void testOneComposedExtendedInterfaceWithOneDefaultParametersEach() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		assertThat(filterParameter.getFormat()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("delete");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("deleted");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(Boolean.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("false");

		filterParameter = stmt2.getClauses().get(0);
		assertThat(filterParameter.getFormat()).isEmpty();
		assertThat(filterParameter.getOperator()).isNotNull().isEqualTo(Equals.class);
		assertThat(filterParameter.isNegate()).isFalse();
		assertThat(filterParameter.getParameters()).isNotEmpty().hasSize(1).contains("status");
		assertThat(filterParameter.getPath()).isNotBlank().isEqualTo("status");
		assertThat(filterParameter.getTargetType()).isNotNull().isEqualTo(StatusEnum.class);
		assertThat(filterParameter.getValues()).isNotEmpty().containsOnly("OK");
	}

	@Test
	public void testAnnotatedInterfaceAndAnnotatedParameter() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(RequiringValues.class, null, Collections.emptyMap());
		assertThat(statement).isNull();
	}

	@Test
	public void testWithSomeRequiredValuesProvided() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
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
