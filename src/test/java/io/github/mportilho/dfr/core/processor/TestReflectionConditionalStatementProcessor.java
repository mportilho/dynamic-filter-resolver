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
import io.github.mportilho.dfr.core.operation.type.Equals;
import io.github.mportilho.dfr.mocks.annotations.AnnotationContainerInterface;
import io.github.mportilho.dfr.mocks.annotations.MethodArgumentAnnotations;
import io.github.mportilho.dfr.mocks.annotations.RequiredStatusValueInterface;
import io.github.mportilho.dfr.mocks.interfaces.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

public class TestReflectionConditionalStatementProcessor {

    @Test
    public void testOneExtendedInterfaceWithOneDefaultParameter() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(NoDelete.class, Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.oppositeStatements()).isEmpty();
        assertThat(statement.clauses()).isNotEmpty().hasSize(1);

        FilterData filterParameter = statement.clauses().get(0);
        assertThat(filterParameter.format()).isEmpty();
        assertThat(filterParameter.operation()).isNotNull().isEqualTo(Equals.class);
        assertThat(filterParameter.negate()).isFalse();
        assertThat(filterParameter.parameters()).isNotEmpty().hasSize(1).contains("delete");
        assertThat(filterParameter.path()).isNotBlank().isEqualTo("deleted");
        assertThat(filterParameter.targetType()).isNotNull().isEqualTo(Boolean.class);
        assertThat(filterParameter.values()).isNotEmpty().containsOnly("false");
    }

    @Test
    public void testOneExtendedInterfaceWithOneRequiredFilterWithoutProvidedValue() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        assertThatThrownBy(() -> provider.createConditionalStatements(RequiredStatusValueInterface.class, Collections.emptyMap()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No value was found for required filter status");
    }

    @Test
    public void testOneExtendedInterfaceWithOneRequiredFilter() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(RequiredStatusValueInterface.class,
                Collections.singletonMap("status", new String[]{"OK"}));

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.oppositeStatements()).isEmpty();
        assertThat(statement.clauses()).isNotEmpty().hasSize(1);

        FilterData filterParameter = statement.clauses().get(0);
        assertThat(filterParameter.format()).isEmpty();
        assertThat(filterParameter.operation()).isNotNull().isEqualTo(Equals.class);
        assertThat(filterParameter.negate()).isFalse();
        assertThat(filterParameter.parameters()).isNotEmpty().hasSize(1).contains("status");
        assertThat(filterParameter.path()).isNotBlank().isEqualTo("status");
        assertThat(filterParameter.targetType()).isNotNull().isEqualTo(String.class);
        assertThat(filterParameter.values()).isNotEmpty().containsOnly("OK");
    }

    @Test
    public void testOneExtendedInterfaceWithOneDefaultParameterNegatingPredicate() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(NoDeleteNegatingPredicate.class, Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.oppositeStatements()).isEmpty();
        assertThat(statement.clauses()).isNotEmpty().hasSize(1);

        FilterData filterParameter = statement.clauses().get(0);
        assertThat(filterParameter.format()).isEmpty();
        assertThat(filterParameter.operation()).isNotNull().isEqualTo(Equals.class);
        assertThat(filterParameter.negate()).isTrue();
        assertThat(filterParameter.parameters()).isNotEmpty().hasSize(1).contains("delete");
        assertThat(filterParameter.path()).isNotBlank().isEqualTo("deleted");
        assertThat(filterParameter.targetType()).isNotNull().isEqualTo(Boolean.class);
        assertThat(filterParameter.values()).isNotEmpty().containsOnly("false");
    }

    @Test
    public void testOneExtendedInterfaceWithOneDefaultParameterWithStringValueResolver() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(str -> str + "1");
        ConditionalStatement statement = provider.createConditionalStatements(NoDelete.class, Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.oppositeStatements()).isEmpty();
        assertThat(statement.clauses()).isNotEmpty().hasSize(1);

        FilterData filterParameter = statement.clauses().get(0);
        assertThat(filterParameter.format()).isEmpty();
        assertThat(filterParameter.operation()).isNotNull().isEqualTo(Equals.class);
        assertThat(filterParameter.negate()).isFalse();
        assertThat(filterParameter.parameters()).isNotEmpty().hasSize(1).contains("delete");
        assertThat(filterParameter.path()).isNotBlank().isEqualTo("deleted");
        assertThat(filterParameter.targetType()).isNotNull().isEqualTo(Boolean.class);
        assertThat(filterParameter.values()).isNotEmpty().containsOnly("false1");
    }

    @Test
    public void testOneExtendedInterfaceWithTwoDefaultParameters() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(NoDeleteAndStatusOk.class, Collections.emptyMap());
        FilterData filterParameter;

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.oppositeStatements()).isEmpty();
        assertThat(statement.clauses()).isNotEmpty().hasSize(2);

        filterParameter = statement.clauses().get(0);
        assertThat(filterParameter.format()).isEmpty();
        assertThat(filterParameter.operation()).isNotNull().isEqualTo(Equals.class);
        assertThat(filterParameter.negate()).isFalse();
        assertThat(filterParameter.parameters()).isNotEmpty().hasSize(1).contains("delete");
        assertThat(filterParameter.path()).isNotBlank().isEqualTo("deleted");
        assertThat(filterParameter.targetType()).isNotNull().isEqualTo(Boolean.class);
        assertThat(filterParameter.values()).isNotEmpty().containsOnly("false");

        filterParameter = statement.clauses().get(1);
        assertThat(filterParameter.format()).isEmpty();
        assertThat(filterParameter.operation()).isNotNull().isEqualTo(Equals.class);
        assertThat(filterParameter.negate()).isFalse();
        assertThat(filterParameter.parameters()).isNotEmpty().hasSize(1).contains("status");
        assertThat(filterParameter.path()).isNotBlank().isEqualTo("status");
        assertThat(filterParameter.targetType()).isNotNull().isEqualTo(StatusEnum.class);
        assertThat(filterParameter.values()).isNotEmpty().containsOnly("OK");
    }

    @Test
    public void testOneComposedExtendedInterfaceWithOneDefaultParametersEach() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(NoDeleteExtendedStatusOK.class, Collections.emptyMap());
        FilterData filterParameter;

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.oppositeStatements()).isNotEmpty().hasSize(2);
        assertThat(statement.clauses()).isEmpty();

        assertThat(statement.findStatementsById("NoDelete")).hasSize(1);
        ConditionalStatement stmt1 = statement.findStatementsById("NoDelete").get(0);
        assertThat(stmt1).isNotNull();
        assertThat(stmt1.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(stmt1.oppositeStatements()).isEmpty();
        assertThat(stmt1.clauses()).isNotEmpty().hasSize(1);

        assertThat(statement.findStatementsById("NoDeleteExtendedStatusOK")).hasSize(1);
        ConditionalStatement stmt2 = statement.findStatementsById("NoDeleteExtendedStatusOK").get(0);
        assertThat(stmt1).isNotNull();
        assertThat(stmt1.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(stmt1.oppositeStatements()).isEmpty();
        assertThat(stmt1.clauses()).isNotEmpty().hasSize(1);

        filterParameter = stmt1.clauses().get(0);
        assertThat(filterParameter.format()).isEmpty();
        assertThat(filterParameter.operation()).isNotNull().isEqualTo(Equals.class);
        assertThat(filterParameter.negate()).isFalse();
        assertThat(filterParameter.parameters()).isNotEmpty().hasSize(1).contains("delete");
        assertThat(filterParameter.path()).isNotBlank().isEqualTo("deleted");
        assertThat(filterParameter.targetType()).isNotNull().isEqualTo(Boolean.class);
        assertThat(filterParameter.values()).isNotEmpty().containsOnly("false");

        filterParameter = stmt2.clauses().get(0);
        assertThat(filterParameter.format()).isEmpty();
        assertThat(filterParameter.operation()).isNotNull().isEqualTo(Equals.class);
        assertThat(filterParameter.negate()).isFalse();
        assertThat(filterParameter.parameters()).isNotEmpty().hasSize(1).contains("status");
        assertThat(filterParameter.path()).isNotBlank().isEqualTo("status");
        assertThat(filterParameter.targetType()).isNotNull().isEqualTo(StatusEnum.class);
        assertThat(filterParameter.values()).isNotEmpty().containsOnly("OK");
    }

    @Test
    public void testAnnotatedInterfaceAndAnnotatedParameter() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);

        ConditionalStatement statement = provider.createConditionalStatements(AnnotationContainerInterface.class,
                MethodArgumentAnnotations.class.getAnnotations(), Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.oppositeStatements()).isNotEmpty().hasSize(4);
        assertThat(statement.clauses()).isEmpty();

        ConditionalStatement searchStatement;

        assertThat(statement.findStatementsById("conjunction_wrapper")).hasSize(1);
        searchStatement = statement.findStatementsById("conjunction_wrapper").get(0);
        assertThat(searchStatement).isNotNull();
        assertThat(searchStatement.logicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(searchStatement.negate()).isFalse();
        assertThat(searchStatement.clauses()).isEmpty();
        assertThat(searchStatement.oppositeStatements()).isNotEmpty().hasSize(4);

        assertThat(statement.findStatementsById("VirtualAnnotationHolder")).hasSize(1);
        searchStatement = statement.findStatementsById("VirtualAnnotationHolder").get(0);
        assertThat(searchStatement).isNotNull();
        assertThat(searchStatement.logicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(searchStatement.negate()).isFalse();
        assertThat(searchStatement.clauses()).isNotEmpty().hasSize(2);
        assertThat(searchStatement.oppositeStatements()).isEmpty();

        assertThat(statement.findStatementsById("DeletedAndError")).hasSize(1);
        searchStatement = statement.findStatementsById("DeletedAndError").get(0);
        assertThat(searchStatement).isNotNull();
        assertThat(searchStatement.logicType()).isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(searchStatement.negate()).isFalse();
        assertThat(searchStatement.clauses()).isNotEmpty().hasSize(2);
        assertThat(searchStatement.oppositeStatements()).isEmpty();

        assertThat(statement.findStatementsById("StatusOk")).hasSize(1);
        searchStatement = statement.findStatementsById("StatusOk").get(0);
        assertThat(searchStatement).isNotNull();
        assertThat(searchStatement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(searchStatement.negate()).isFalse();
        assertThat(searchStatement.clauses()).isNotEmpty().hasSize(1);
        assertThat(searchStatement.oppositeStatements()).isEmpty();

        assertThat(statement.findStatementsById("NotDeleted")).hasSize(1);
        searchStatement = statement.findStatementsById("NotDeleted").get(0);
        assertThat(searchStatement).isNotNull();
        assertThat(searchStatement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(searchStatement.negate()).isFalse();
        assertThat(searchStatement.clauses()).isNotEmpty().hasSize(1);
        assertThat(searchStatement.oppositeStatements()).isEmpty();
    }

    @Test
    public void testWithRequiredValuesProvided() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement;

        statement = provider.createConditionalStatements(RequiringValues.class, Collections.singletonMap("delete", new String[]{"true"}));

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(statement.clauses().get(0).values()).isNotEmpty().hasSize(1).contains("true");
        assertThat(statement.oppositeStatements()).isEmpty();

        statement = provider.createConditionalStatements(RequiringValues.class, Collections.singletonMap("delete", new String[]{"false"}));

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(statement.clauses().get(0).values()).isNotEmpty().hasSize(1).contains("false");
        assertThat(statement.oppositeStatements()).isEmpty();
    }

    @Test
    public void testWithRequiredValuesNotProvided() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(RequiringValues.class, Collections.emptyMap());
        assertThat(statement).isNull();
    }

    @Test
    public void testWithSomeRequiredValuesProvided() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeValues.class, Collections.emptyMap());
        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotEmpty().hasSize(1);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.oppositeStatements()).isEmpty();
    }

    @Test
    public void testWithSomeMoreRequiredValuesProvided() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeMoreValues.class, Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotEmpty().hasSize(1);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.oppositeStatements()).isNotEmpty().hasSize(1);

        ConditionalStatement or1 = statement.oppositeStatements().get(0);
        assertThat(or1).isNotNull();
        assertThat(or1.logicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(or1.negate()).isFalse();
        assertThat(or1.clauses()).isNotEmpty().hasSize(1);
        assertThat(or1.clauses().get(0).path()).isEqualTo("weight");
        assertThat(or1.oppositeStatements()).isEmpty();
    }

    @Test
    public void testWithSomeMoreRequiredValuesAndSomeDefaultsProvided() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(RequiringSomeValuesAndSomeDefaults.class, Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotEmpty().hasSize(1);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.oppositeStatements()).isNotEmpty().hasSize(2);

        assertThat(statement.findStatementsById("RequiringSomeValuesAndSomeDefaults_subStatements_0")).hasSize(1);
        ConditionalStatement or1 = statement.findStatementsById("RequiringSomeValuesAndSomeDefaults_subStatements_0").get(0);
        assertThat(or1).isNotNull();
        assertThat(or1.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(or1.negate()).isFalse();
        assertThat(or1.clauses()).isNotEmpty().hasSize(1);
        assertThat(or1.clauses().get(0).path()).isEqualTo("birthday");
        assertThat(or1.oppositeStatements()).isEmpty();

        assertThat(statement.findStatementsById("RequiringSomeValuesAndSomeDefaults_subStatements_1")).hasSize(1);
        ConditionalStatement or2 = statement.findStatementsById("RequiringSomeValuesAndSomeDefaults_subStatements_1").get(0);
        assertThat(or2).isNotNull();
        assertThat(or2.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(or2.negate()).isFalse();
        assertThat(or2.clauses()).isNotEmpty().hasSize(1);
        assertThat(or2.clauses().get(0).path()).isEqualTo("student");
        assertThat(or2.oppositeStatements()).isEmpty();
    }

    @Test
    public void testRequiredValuesWithDefaultDataProvided() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement;

        statement = provider.createConditionalStatements(RequiringValuesDefaultData.class, Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.clauses().get(0).values()).isNotEmpty().hasSize(1).contains("true");
        assertThat(statement.oppositeStatements()).isEmpty();

        statement = provider.createConditionalStatements(RequiringValues.class, Collections.singletonMap("delete", new String[]{"false"}));

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.clauses().get(0).values()).isNotEmpty().hasSize(1).contains("false");
        assertThat(statement.oppositeStatements()).isEmpty();
    }

    @Test
    public void testWithConjunctionAnnotationFullyProvided() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringConjunction.class, Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotEmpty().hasSize(2);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.clauses().get(1).path()).isEqualTo("name");
        assertThat(statement.oppositeStatements()).isNotEmpty().hasSize(2);

        assertThat(statement.findStatementsById("FullyRequiringConjunction_subStatements_0")).hasSize(1);
        ConditionalStatement or1 = statement.findStatementsById("FullyRequiringConjunction_subStatements_0").get(0);
        assertThat(or1).isNotNull();
        assertThat(or1.logicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(or1.negate()).isFalse();
        assertThat(or1.clauses()).isNotEmpty().hasSize(1);
        assertThat(or1.clauses().get(0).path()).isEqualTo("status");
        assertThat(or1.oppositeStatements()).isEmpty();

        assertThat(statement.findStatementsById("FullyRequiringConjunction_subStatements_1")).hasSize(1);
        ConditionalStatement or2 = statement.findStatementsById("FullyRequiringConjunction_subStatements_1").get(0);
        assertThat(or2).isNotNull();
        assertThat(or2.logicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(or2.negate()).isFalse();
        assertThat(or2.clauses()).isNotEmpty().hasSize(2);
        assertThat(or2.clauses().get(0).path()).isEqualTo("birthday");
        assertThat(or2.clauses().get(1).path()).isEqualTo("height");
        assertThat(or2.oppositeStatements()).isEmpty();
    }

    @Test
    public void testWithConjunctionAnnotationFullyProvidedNegatingAll() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringConjunctionNegatingAll.class, Collections.emptyMap());

        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isTrue();
        assertThat(statement.clauses()).isNotEmpty().hasSize(2);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.clauses().get(1).path()).isEqualTo("name");
        assertThat(statement.oppositeStatements()).isNotEmpty().hasSize(2);

        ConditionalStatement or1 = statement.oppositeStatements().get(0);
        assertThat(or1).isNotNull();
        assertThat(or1.logicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(or1.negate()).isTrue();
        assertThat(or1.clauses()).isNotEmpty().hasSize(1);
        assertThat(or1.clauses().get(0).path()).isEqualTo("status");
        assertThat(or1.oppositeStatements()).isEmpty();

        ConditionalStatement or2 = statement.oppositeStatements().get(1);
        assertThat(or2).isNotNull();
        assertThat(or2.logicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(or2.negate()).isTrue();
        assertThat(or2.clauses()).isNotEmpty().hasSize(2);
        assertThat(or2.clauses().get(0).path()).isEqualTo("birthday");
        assertThat(or2.clauses().get(1).path()).isEqualTo("height");
        assertThat(or2.oppositeStatements()).isEmpty();
    }

    @Test
    public void testWithDisjunctionAnnotationFullyProvided() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringDisjunction.class, Collections.emptyMap());
        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotEmpty().hasSize(2);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.clauses().get(1).path()).isEqualTo("name");
        assertThat(statement.oppositeStatements()).isNotEmpty().hasSize(2);

        ConditionalStatement or1 = statement.oppositeStatements().get(0);
        assertThat(or1).isNotNull();
        assertThat(or1.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(or1.negate()).isFalse();
        assertThat(or1.clauses()).isNotEmpty().hasSize(1);
        assertThat(or1.clauses().get(0).path()).isEqualTo("status");
        assertThat(or1.oppositeStatements()).isEmpty();

        ConditionalStatement or2 = statement.oppositeStatements().get(1);
        assertThat(or2).isNotNull();
        assertThat(or2.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(or2.negate()).isFalse();
        assertThat(or2.clauses()).isNotEmpty().hasSize(2);
        assertThat(or2.clauses().get(0).path()).isEqualTo("birthday");
        assertThat(or2.clauses().get(1).path()).isEqualTo("height");
        assertThat(or2.oppositeStatements()).isEmpty();
    }

    @Test
    public void testWithDisjunctionAnnotationFullyProvidedNegatingAll() {
        ReflectionConditionalStatementProcessor provider = new ReflectionConditionalStatementProcessor(null);
        ConditionalStatement statement = provider.createConditionalStatements(FullyRequiringDisjunctionNegatingAll.class, Collections.emptyMap());
        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.DISJUNCTION);
        assertThat(statement.negate()).isTrue();
        assertThat(statement.clauses()).isNotEmpty().hasSize(2);
        assertThat(statement.clauses().get(0).path()).isEqualTo("deleted");
        assertThat(statement.clauses().get(1).path()).isEqualTo("name");
        assertThat(statement.oppositeStatements()).isNotEmpty().hasSize(2);

        ConditionalStatement or1 = statement.oppositeStatements().get(0);
        assertThat(or1).isNotNull();
        assertThat(or1.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(or1.negate()).isTrue();
        assertThat(or1.clauses()).isNotEmpty().hasSize(1);
        assertThat(or1.clauses().get(0).path()).isEqualTo("status");
        assertThat(or1.oppositeStatements()).isEmpty();

        ConditionalStatement or2 = statement.oppositeStatements().get(1);
        assertThat(or2).isNotNull();
        assertThat(or2.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(or2.negate()).isTrue();
        assertThat(or2.clauses()).isNotEmpty().hasSize(2);
        assertThat(or2.clauses().get(0).path()).isEqualTo("birthday");
        assertThat(or2.clauses().get(1).path()).isEqualTo("height");
        assertThat(or2.oppositeStatements()).isEmpty();
    }

}
