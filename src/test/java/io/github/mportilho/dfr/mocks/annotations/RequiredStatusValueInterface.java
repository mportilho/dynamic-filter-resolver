package io.github.mportilho.dfr.mocks.annotations;


import io.github.mportilho.dfr.core.processor.annotation.Conjunction;
import io.github.mportilho.dfr.core.processor.annotation.Filter;
import io.github.mportilho.dfr.core.operation.type.Equals;

@Conjunction(value = {
        @Filter(path = "status", parameters = "status", operation = Equals.class, required = true, targetType = String.class)
})
public interface RequiredStatusValueInterface {
}
