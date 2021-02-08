package io.github.mportilho.apptests.app.queries;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;
import io.github.mportilho.dfr.providers.specification.annotation.Fetching;

@Fetching({ "phones" })
@Conjunction(
	value = { 
		@Filter(path = "addresses.number", parameters = "houseNumber", operator = GreaterOrEquals.class, defaultValues = "1010") 
	}
)
public interface FetchingSimplePath {

}
