package io.github.mportilho.dfr.mocks.interfaces;


import io.github.mportilho.dfr.core.operation.type.Between;
import io.github.mportilho.dfr.core.operation.type.Equals;
import io.github.mportilho.dfr.core.operation.type.Like;
import io.github.mportilho.dfr.core.processor.annotation.Disjunction;
import io.github.mportilho.dfr.core.processor.annotation.Filter;
import io.github.mportilho.dfr.core.processor.annotation.Statement;

@Disjunction(value = {
        @Filter(path = "isbn", parameters = "isbn", operation = Equals.class)
},
        conjunctions = {
                @Statement({
                        @Filter(path = "author", parameters = "author", operation = Like.class, ignoreCase = true),
                        @Filter(path = "publishingDate", parameters = {"publishedFrom", "publishedTo"}, operation = Between.class, required = true),
                        @Filter(path = "releaseState", parameters = "releaseState", operation = Equals.class, negate = "${VAR_NEG_STATE}", defaultValues = "0")
                })
        }
)
public interface SearchBooks {
}
