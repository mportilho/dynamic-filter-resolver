package io.github.mportilho.dfr.core.processor;

import io.github.mportilho.dfr.core.processor.annotation.AnnotationConditionalStatementProcessor;
import io.github.mportilho.dfr.core.processor.annotation.AnnotationProcessorParameter;
import io.github.mportilho.dfr.core.processor.expressionresolver.StringValueExpressionResolver;
import io.github.mportilho.dfr.mocks.ObjectValueExpressionResolver;
import io.github.mportilho.dfr.mocks.interfaces.SearchGames;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TestValueExpressionResolver_OnStatementProcessor {

    @Test
    public void test_FailOn_EmptyRequiredParameter() {
        Map<String, String> valueExpressionResolverDataMap = Map.of(
                "${ON_SALE}", "false",
                "${SPECIAL_CLIENT}", "true");
        StringValueExpressionResolver resolver = new StringValueExpressionResolver(valueExpressionResolverDataMap);
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor = new AnnotationConditionalStatementProcessor(resolver);

        Assertions.assertThatThrownBy(() ->
                        processor.createStatements(new AnnotationProcessorParameter(SearchGames.class, null), Map.of()))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Parameter 'genre' required");
        ;

    }

    @Test
    public void test_SuccessOn_ProvidedRequiredParameter() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = Map.of(
                "${ON_SALE}", "false",
                "${SPECIAL_CLIENT}", "true",
                "${MIN_DATE}", LocalDate.of(2020, 1, 1),
                "${MAX_DATE}", LocalDate.of(2020, 12, 31));
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        userParameters = Map.of(
                "genre", new String[]{"rpg"},
                "minPrice", new Object[]{new BigDecimal("12")},
                "maxPrice", new Object[]{new BigDecimal("20")},
                "tags", new String[]{"most_bought", "popular", "awarded"});


        statement = processor.createStatements(new AnnotationProcessorParameter(SearchGames.class, null), userParameters);
        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotEmpty().hasSize(6);

        assertThat(statement.findClauseByPath("genre"))
                .isNotEmpty()
                .get().extracting("values").asInstanceOf(InstanceOfAssertFactories.list(Object[].class))
                .containsExactly(new String[]{"rpg"});

        assertThat(statement.findClauseByPath("onSale"))
                .isNotEmpty()
                .get().extracting("values").asInstanceOf(InstanceOfAssertFactories.list(Object[].class))
                .containsExactly(new String[]{"false"});

        assertThat(statement.findClauseByPath("specialClient"))
                .isNotEmpty()
                .get().extracting("values").asInstanceOf(InstanceOfAssertFactories.list(Object[].class))
                .containsExactly(new String[]{"true"});

        assertThat(statement.findClauseByPath("priceInterval"))
                .isNotEmpty()
                .get().extracting("values").asInstanceOf(InstanceOfAssertFactories.list(Object[].class))
                .containsExactly(new BigDecimal[]{new BigDecimal("12")}, new BigDecimal[]{new BigDecimal("20")});

        assertThat(statement.findClauseByPath("dateSearchInterval"))
                .isNotEmpty()
                .get().extracting("values").asInstanceOf(InstanceOfAssertFactories.list(Object[].class))
                .containsExactly(new LocalDate[]{LocalDate.of(2020, 1, 1)},
                        new LocalDate[]{LocalDate.of(2020, 12, 31)});

        assertThat(statement.findClauseByPath("tags"))
                .isNotEmpty()
                .get().extracting("values").asInstanceOf(InstanceOfAssertFactories.list(Object[].class))
                .containsExactly(new String[]{"most_bought", "popular", "awarded"});
    }

    private List<Object[]> wrap(Object[] obj) {
        return List.<Object[]>of(obj);
    }

}
