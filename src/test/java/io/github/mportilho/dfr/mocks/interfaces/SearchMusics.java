package io.github.mportilho.dfr.mocks.interfaces;

import io.github.mportilho.dfr.core.operation.type.Equals;
import io.github.mportilho.dfr.core.operation.type.GreaterOrEquals;
import io.github.mportilho.dfr.core.operation.type.Like;
import io.github.mportilho.dfr.core.processor.annotation.Conjunction;
import io.github.mportilho.dfr.core.processor.annotation.Filter;

@Conjunction(value = {
        @Filter(path = "author", parameters = "author", operation = Equals.class, modifiers = {"mod1=modValue1"}),
        @Filter(path = "genre", parameters = "genre", operation = Equals.class, modifiers = {"mod2=modValue2", "mod3=modValue3"}),
        @Filter(path = "releaseDate", parameters = "releaseDate", operation = GreaterOrEquals.class, format = "dd-MM/yyyy"),
        @Filter(path = "trackQuery", parameters = "trackQuery", operation = Like.class, format = "${TRACK_REGEX}"),
})
public interface SearchMusics {
}
