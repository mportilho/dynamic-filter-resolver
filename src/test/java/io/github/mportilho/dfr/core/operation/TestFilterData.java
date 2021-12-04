package io.github.mportilho.dfr.core.operation;

import io.github.mportilho.dfr.core.operation.type.Equals;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.Map;

public class TestFilterData {

    @Test
    public void testFilterDataMethods() {
        FilterData fdNoValues = new FilterData("ap", "p", new String[]{"p1", "p2"}, null,
                Equals.class, false, false, null, null, null);
        FilterData fdSingleValues = new FilterData("ap", "p", new String[]{"p1", "p2"}, null,
                Equals.class, false, false, new Integer[]{1}, null, Map.of("JoinType", "INNER"));
        FilterData fdMultipleValues = new FilterData("ap", "p", new String[]{"p1", "p2"}, null,
                Equals.class, false, false, new Integer[]{1, 2, 3}, null,
                Map.of("a", "1", "b", "2"));

        assertThat(fdNoValues.values()).isNull();
        assertThat(fdNoValues.findOneValue()).isNull();
        assertThat(fdNoValues.findModifier("teste")).isNull();

        assertThat(fdSingleValues.values()).isNotEmpty().containsExactly(1);
        assertThat(fdSingleValues.findOneValue()).isNotNull().isEqualTo(1);
        assertThat(fdSingleValues.findModifier("teste")).isNull();
        assertThat(fdSingleValues.findModifier("JoinType")).isEqualTo("INNER");

        assertThat(fdMultipleValues.values()).isNotEmpty().containsExactly(1, 2, 3);
        assertThatThrownBy(fdMultipleValues::findOneValue).isInstanceOf(IllegalStateException.class)
                .hasMessage("Multiple values found while fetching a single one");
        assertThat(fdMultipleValues.findModifier("teste")).isNull();
        assertThat(fdMultipleValues.findModifier("a")).isEqualTo("1");
        assertThat(fdMultipleValues.findModifier("b")).isEqualTo("2");
    }

}
