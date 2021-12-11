package io.github.mportilho.dfr.core.processor;

import io.github.mportilho.dfr.core.processor.expressionresolver.StringValueExpressionResolver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

public class TestStringValueExpressionResolver {

    @Test
    public void testEmptyResolver() {
        StringValueExpressionResolver resolver;

        resolver = new StringValueExpressionResolver(null);
        Assertions.assertThat(resolver.resolveValue("nothing")).isNull();

        resolver = new StringValueExpressionResolver(Collections.emptyMap(), null);
        Assertions.assertThat(resolver.resolveValue("nothing")).isNull();
    }

    @Test
    public void testMultipleUsages() {
        StringValueExpressionResolver resolver;
        Map<String, String> map = Map.of("a", "1", "b", "2");

        resolver = new StringValueExpressionResolver(map, null);
        Assertions.assertThat(resolver.resolveValue(null)).isNull();
        Assertions.assertThat(resolver.resolveValue("nothing")).isNull();
        Assertions.assertThat(resolver.resolveValue("a")).isEqualTo("1");
        Assertions.assertThat(resolver.resolveValue("b")).isEqualTo("2");
    }

    @Test
    public void testDelegate() {
        StringValueExpressionResolver resolver;
        Map<String, String> normalMap = Map.of("a", "1");
        Map<String, String> delegateMap = Map.of("a", "99", "b", "2");

        resolver = new StringValueExpressionResolver(normalMap, new StringValueExpressionResolver(delegateMap));
        Assertions.assertThat(resolver.resolveValue(null)).isNull();
        Assertions.assertThat(resolver.resolveValue("nothing")).isNull();
        Assertions.assertThat(resolver.resolveValue("a")).isEqualTo("1");
        Assertions.assertThat(resolver.resolveValue("b")).isEqualTo("2");
    }

}
