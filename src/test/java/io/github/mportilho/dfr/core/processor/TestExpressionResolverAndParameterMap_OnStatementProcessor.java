package io.github.mportilho.dfr.core.processor;

import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.processor.annotation.AnnotationConditionalStatementProcessor;
import io.github.mportilho.dfr.core.processor.annotation.AnnotationProcessorParameter;
import io.github.mportilho.dfr.core.processor.expressionresolver.StringValueExpressionResolver;
import io.github.mportilho.dfr.mocks.ObjectValueExpressionResolver;
import io.github.mportilho.dfr.mocks.interfaces.SearchBooks;
import io.github.mportilho.dfr.mocks.interfaces.SearchGames;
import io.github.mportilho.dfr.mocks.interfaces.SearchMusics;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TestExpressionResolverAndParameterMap_OnStatementProcessor {

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
    }

    @Test
    public void test_FailOn_EmptyRequiredDoubleParameters() {
        Map<String, String> valueExpressionResolverDataMap = Map.of(
                "${ON_SALE}", "false",
                "${SPECIAL_CLIENT}", "true");
        StringValueExpressionResolver resolver = new StringValueExpressionResolver(valueExpressionResolverDataMap);
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor = new AnnotationConditionalStatementProcessor(resolver);

        Assertions.assertThatThrownBy(() ->
                        processor.createStatements(new AnnotationProcessorParameter(SearchGames.class, null), Map.of()))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Parameter 'genre' required");
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

    @Test
    public void test_failOn_NotProvided_RequiredDoubleParameters() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;

        valueExpressionResolverDataMap = Map.of();
        userParameters = Map.of("isbn", new String[]{"88559657"});
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        assertThatThrownBy(() ->
                processor.createStatements(new AnnotationProcessorParameter(SearchBooks.class, null), userParameters))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Parameters 'publishedFrom, publishedTo' required");
    }

    @Test
    public void test_successOn_NotProvided_NegateParameterVariable() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = Map.of();
        userParameters = Map.of(
                "isbn", new String[]{"88559657"},
                "publishedFrom", new LocalDate[]{LocalDate.of(2000, 1, 1)},
                "publishedTo", new LocalDate[]{LocalDate.of(2020, 12, 31)}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        statement = processor.createStatements(new AnnotationProcessorParameter(SearchBooks.class, null), userParameters);
        assertThat(statement).isNotNull();

        ConditionalStatement subStatement = statement.oppositeStatements().get(0);
        assertThat(subStatement).isNotNull();

        FilterData filterData = subStatement.findClauseByPath("releaseState").orElseThrow();
        assertThat(filterData.negate()).isFalse();
    }

    @Test
    public void test_successOn_providing_NegateParameterVariable() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = Map.of(
                "${VAR_NEG_STATE}", true
        );
        userParameters = Map.of(
                "isbn", new String[]{"88559657"},
                "publishedFrom", new LocalDate[]{LocalDate.of(2000, 1, 1)},
                "publishedTo", new LocalDate[]{LocalDate.of(2020, 12, 31)},
                "releaseState", new Integer[]{1}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        statement = processor.createStatements(new AnnotationProcessorParameter(SearchBooks.class, null), userParameters);
        assertThat(statement).isNotNull();

        ConditionalStatement subStatement = statement.oppositeStatements().get(0);
        assertThat(subStatement).isNotNull();

        FilterData filterData = subStatement.findClauseByPath("releaseState").orElseThrow();
        assertThat(filterData.negate()).isTrue();
    }

    @Test
    public void test_successOn_assertFalseValue_providingInvalidDataFor_NegateParameterVariable() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = Map.of(
                "${VAR_NEG_STATE}", new String[]{"invalidData"}
        );
        userParameters = Map.of(
                "isbn", new String[]{"88559657"},
                "publishedFrom", new LocalDate[]{LocalDate.of(2000, 1, 1)},
                "publishedTo", new LocalDate[]{LocalDate.of(2020, 12, 31)},
                "releaseState", new Integer[]{1}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        statement = processor.createStatements(new AnnotationProcessorParameter(SearchBooks.class, null), userParameters);
        assertThat(statement).isNotNull();

        ConditionalStatement subStatement = statement.oppositeStatements().get(0);
        assertThat(subStatement).isNotNull();

        FilterData filterData = subStatement.findClauseByPath("releaseState").orElseThrow();
        assertThat(filterData.negate()).isFalse();
    }

    @Test
    public void test_successOn_assertFalseValue_providingNullFor_NegateParameterVariable() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = new HashMap<>();
        valueExpressionResolverDataMap.put("${VAR_NEG_STATE}", null);

        userParameters = Map.of(
                "isbn", new String[]{"88559657"},
                "publishedFrom", new LocalDate[]{LocalDate.of(2000, 1, 1)},
                "publishedTo", new LocalDate[]{LocalDate.of(2020, 12, 31)},
                "releaseState", new Integer[]{1}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        statement = processor.createStatements(new AnnotationProcessorParameter(SearchBooks.class, null), userParameters);
        assertThat(statement).isNotNull();

        ConditionalStatement subStatement = statement.oppositeStatements().get(0);
        assertThat(subStatement).isNotNull();

        FilterData filterData = subStatement.findClauseByPath("releaseState").orElseThrow();
        assertThat(filterData.negate()).isFalse();
    }

    @Test
    public void test_failOn_providingMoreThenOneValueFor_NegateParameterVariable() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;

        valueExpressionResolverDataMap = Map.of(
                "${VAR_NEG_STATE}", new String[]{"true", "false"}
        );
        userParameters = Map.of(
                "isbn", new String[]{"88559657"},
                "publishedFrom", new LocalDate[]{LocalDate.of(2000, 1, 1)},
                "publishedTo", new LocalDate[]{LocalDate.of(2020, 12, 31)},
                "releaseState", new Integer[]{1}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        assertThatThrownBy(() -> processor.createStatements(new AnnotationProcessorParameter(SearchBooks.class, null), userParameters))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Attribute 'negate' parsing produced more than one value for element releaseState");
    }

    @Test
    public void test_successOn_readingProvidedFilterModifiers() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = new HashMap<>();

        userParameters = Map.of(
                "author", new String[]{"Fulano"},
                "genre", new String[]{"heavy metal"},
                "releaseDate", new String[]{"05-06/2017"}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        statement = processor.createStatements(new AnnotationProcessorParameter(SearchMusics.class, null), userParameters);
        assertThat(statement).isNotNull();
        assertThat(statement.clauses()).isNotEmpty().hasSize(3);
        assertThat(statement.findClauseByPath("author").orElseThrow().modifiers()).isNotEmpty().hasSize(1)
                .containsAllEntriesOf(Map.of("mod1", "modValue1"));
        assertThat(statement.findClauseByPath("genre").orElseThrow().modifiers()).isNotEmpty().hasSize(2)
                .containsAllEntriesOf(Map.of("mod2", "modValue2", "mod3", "modValue3"));
        assertThat(statement.findClauseByPath("releaseDate").orElseThrow().modifiers()).isEmpty();
    }

    @Test
    public void test_failOn_providingMoreThanOneFilterFormat() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;

        valueExpressionResolverDataMap = Map.of(
                "${TRACK_REGEX}", new String[]{"^[a-z0-9-.]+$", "^[a-z]+$"}
        );

        userParameters = Map.of(
                "author", new String[]{"Fulano"},
                "genre", new String[]{"heavy metal"},
                "releaseDate", new String[]{"05-06/2017"},
                "trackQuery", new String[]{"warriors of"}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        assertThatThrownBy(() -> processor.createStatements(new AnnotationProcessorParameter(SearchMusics.class, null), userParameters))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Attribute 'format' parsing produced more than one value for path trackQuery");
    }

    @Test
    public void test_successOn_readingProvidedFilterFormats() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = Map.of(
                "${TRACK_REGEX}", "^[a-z0-9-.]+$"
        );

        userParameters = Map.of(
                "author", new String[]{"Fulano"},
                "genre", new String[]{"heavy metal"},
                "releaseDate", new String[]{"05-06/2017"},
                "trackQuery", new String[]{"warriors of"}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        statement = processor.createStatements(new AnnotationProcessorParameter(SearchMusics.class, null), userParameters);
        assertThat(statement).isNotNull();
        assertThat(statement.clauses()).isNotEmpty().hasSize(4);
        assertThat(statement.findClauseByPath("author").orElseThrow().format()).isEmpty();
        assertThat(statement.findClauseByPath("genre").orElseThrow().format()).isEmpty();
        assertThat(statement.findClauseByPath("releaseDate").orElseThrow().format()).isNotEmpty().isEqualTo("dd-MM/yyyy");
        assertThat(statement.findClauseByPath("trackQuery").orElseThrow().format()).isNotEmpty().isEqualTo("^[a-z0-9-.]+$");
    }

    @Test
    public void test_successOn_readingProvidedOneValuedArray_ForFormatAttribute() {
        ObjectValueExpressionResolver resolver;
        Map<String, Object> valueExpressionResolverDataMap;
        Map<String, Object[]> userParameters;
        ConditionalStatementProcessor<AnnotationProcessorParameter> processor;
        ConditionalStatement statement;

        valueExpressionResolverDataMap = Map.of(
                "${TRACK_REGEX}", new String[]{"^[a-z0-9-.]+$"}
        );

        userParameters = Map.of(
                "author", new String[]{"Fulano"},
                "genre", new String[]{"heavy metal"},
                "releaseDate", new String[]{"05-06/2017"},
                "trackQuery", new String[]{"warriors of"}
        );
        resolver = new ObjectValueExpressionResolver(valueExpressionResolverDataMap);
        processor = new AnnotationConditionalStatementProcessor(resolver);

        statement = processor.createStatements(new AnnotationProcessorParameter(SearchMusics.class, null), userParameters);
        assertThat(statement).isNotNull();
        assertThat(statement.clauses()).isNotEmpty().hasSize(4);
        assertThat(statement.findClauseByPath("author").orElseThrow().format()).isEmpty();
        assertThat(statement.findClauseByPath("genre").orElseThrow().format()).isEmpty();
        assertThat(statement.findClauseByPath("releaseDate").orElseThrow().format()).isNotEmpty().isEqualTo("dd-MM/yyyy");
        assertThat(statement.findClauseByPath("trackQuery").orElseThrow().format()).isNotEmpty().isEqualTo("^[a-z0-9-.]+$");
    }

}
