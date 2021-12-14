package io.github.mportilho.dfr.mocks.interfaces;

import io.github.mportilho.dfr.core.processor.annotation.Conjunction;
import io.github.mportilho.dfr.core.processor.annotation.Filter;
import io.github.mportilho.dfr.core.operation.type.Between;
import io.github.mportilho.dfr.core.operation.type.Equals;
import io.github.mportilho.dfr.core.operation.type.IsIn;

import java.math.BigDecimal;
import java.time.LocalDate;

@Conjunction(value = {
        @Filter(path = "genre", parameters = "genre", operation = Equals.class, targetType = String.class, required = true),
        @Filter(path = "onSale", parameters = "onSale", operation = Equals.class, targetType = String.class, defaultValues = "${ON_SALE}"),
        @Filter(path = "specialClient", parameters = "specialClient", operation = Equals.class, targetType = Boolean.class, constantValues = "${SPECIAL_CLIENT}"),
        @Filter(path = "dateSearchInterval", parameters = {"minDate", "maxDate"}, operation = Between.class, targetType = LocalDate.class, defaultValues = {"${MIN_DATE}", "${MAX_DATE}"}),
        @Filter(path = "priceInterval", parameters = {"minPrice", "maxPrice"}, operation = Between.class, targetType = BigDecimal.class),
        @Filter(path = "tags", parameters = "tags", operation = IsIn.class, targetType = String.class, defaultValues = {"on_stock", "reviewed"})
})
public interface SearchGames {
}
