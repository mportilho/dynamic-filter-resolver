package io.github.mportilho.dfr.core.processor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestValueExpressionResolver {

    @Test
    public void testValueExpressionResolver() {
        Map<String, Object[]> map = Map.of(
                "Seasons", new String[]{"summer", "spring", "autumn", "winter"},
                "square", new Integer[]{4},
                "nothing", new Object[]{});

//        ValueExpressionResolver resolver = new StringValueExpressionResolver(map, null);

//        Assertions.assertThat(resolver.resolveValue("Seasons"));
    }

}
