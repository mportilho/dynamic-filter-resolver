package com.github.dfr.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.github.dfr.filter.ConditionalStatement;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.LogicType;
import com.github.dfr.operator.type.Equals;
import com.github.dfr.provider.interfaces.NoDeleteAndStatusOkSpecification;
import com.github.dfr.provider.interfaces.NoDeleteExtendedStatusOKSpecification;
import com.github.dfr.provider.interfaces.NoDeleteSpecification;
import com.github.dfr.provider.interfaces.StatusEnum;

public class TestAnnotationContextProvider {

	@Test
	public void testOneExtendedInterfaceWithOneDefaultParameter() {
		AnnotationBasedFilterLogicContextProvider provider = new AnnotationBasedFilterLogicContextProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteSpecification.class, Collections.emptyMap());

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
		AnnotationBasedFilterLogicContextProvider provider = new AnnotationBasedFilterLogicContextProvider(str -> str + "1");
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteSpecification.class, Collections.emptyMap());

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
		AnnotationBasedFilterLogicContextProvider provider = new AnnotationBasedFilterLogicContextProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteAndStatusOkSpecification.class, Collections.emptyMap());
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
		AnnotationBasedFilterLogicContextProvider provider = new AnnotationBasedFilterLogicContextProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(NoDeleteExtendedStatusOKSpecification.class, Collections.emptyMap());
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

}
