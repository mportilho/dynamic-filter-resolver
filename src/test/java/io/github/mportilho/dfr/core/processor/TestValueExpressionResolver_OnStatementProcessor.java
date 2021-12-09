package io.github.mportilho.dfr.core.processor;

import io.github.mportilho.dfr.core.processor.impl.ReflectionConditionalStatementProcessor;
import io.github.mportilho.dfr.core.processor.impl.ReflectionParameter;
import io.github.mportilho.dfr.core.processor.impl.StringValueExpressionResolver;
import io.github.mportilho.dfr.mocks.ObjectValueExpressionResolver;
import io.github.mportilho.dfr.mocks.interfaces.SearchGames;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

public class TestValueExpressionResolver_OnStatementProcessor {

    @Test
    public void testValueExpressionResolver() {
        Map<String, Object[]> map = Map.of(
                "Seasons", new String[]{"summer", "spring", "autumn", "winter"},
                "square", new Integer[]{4},
                "nothing", new Object[]{});

//        ValueExpressionResolver resolver = new StringValueExpressionResolver(map, null);

//        Assertions.assertThat(resolver.resolveValue("Seasons"));
    }

    @Test
    public void test_FailOn_EmptyRequiredParameter() {
        Map<String, String> valueExpressionResolverDataMap = Map.of(
                "${ON_SALE}", "false",
                "${SPECIAL_CLIENT}", "true");
        StringValueExpressionResolver resolver = new StringValueExpressionResolver(valueExpressionResolverDataMap);
        ConditionalStatementProcessor<ReflectionParameter> processor = new ReflectionConditionalStatementProcessor(resolver);

        Assertions.assertThatThrownBy(() ->
                        processor.createConditionalStatements(new ReflectionParameter(SearchGames.class, null), Map.of()))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("of path 'genre'");
        ;

    }

    @Test
    public void test_SuccessOn_ProvidedRequiredParameter() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<ReflectionParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = Map.of(
                "${ON_SALE}", "false",
                "${SPECIAL_CLIENT}", "true",
                "${MIN_DATE}", LocalDate.of(2020, 1, 1),
                "${MAX_DATE}", LocalDate.of(2020, 12, 31));
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new ReflectionConditionalStatementProcessor(resolver);

        userParameters = Map.of(
                "genre", new String[]{"rpg"},
                "minPrice", new BigDecimal[]{new BigDecimal("12")},
                "maxPrice", new BigDecimal[]{new BigDecimal("20")},
                "tags", new Object[]{new String[]{"most_bought", "popular", "awarded"}});


        statement = processor.createConditionalStatements(new ReflectionParameter(SearchGames.class, null), userParameters);
        assertThat(statement).isNotNull();
        assertThat(statement.logicType()).isNotNull().isEqualByComparingTo(LogicType.CONJUNCTION);
        assertThat(statement.negate()).isFalse();
        assertThat(statement.clauses()).isNotEmpty().hasSize(6);

        assertThat(statement.findClauseByPath("genre"))
                .isNotEmpty().get()
                .extracting("values").isEqualTo(wrapArray(new String[]{"rpg"}));

        assertThat(statement.findClauseByPath("onSale"))
                .isNotEmpty().get()
                .extracting("values").isEqualTo(wrapArray(new String[]{"false"}));

        assertThat(statement.findClauseByPath("specialClient"))
                .isNotEmpty().get()
                .extracting("values").isEqualTo(wrapArray(new String[]{"true"}));

        assertThat(statement.findClauseByPath("priceInterval"))
                .isNotEmpty().get()
                .extracting("values").isEqualTo(Arrays.asList(
                                new BigDecimal[]{new BigDecimal("12")},
                                new BigDecimal[]{new BigDecimal("20")})
                        .toArray());

        assertThat(statement.findClauseByPath("dateSearchInterval"))
                .isNotEmpty().get()
                .extracting("values").isEqualTo(Arrays.asList(
                                new LocalDate[]{LocalDate.of(2020, 1, 1)},
                                new LocalDate[]{LocalDate.of(2020, 12, 31)})
                        .toArray());

        assertThat(statement.findClauseByPath("tags"))
                .isNotEmpty().get()
                .extracting("values").isEqualTo(new Object[]{new String[]{"most_bought", "popular", "awarded"}});
    }

    private Object[] wrapArray(Object obj) {
        return new Object[]{obj};
    }

}
