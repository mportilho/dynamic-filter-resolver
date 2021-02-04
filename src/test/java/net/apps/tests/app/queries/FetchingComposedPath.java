package net.apps.tests.app.queries;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.operator.type.GreaterOrEquals;
import net.dfr.providers.specification.annotation.Fetching;

@Fetching({ "addresses.location" })
@Conjunction(
	value = { 
		@Filter(path = "addresses.number", parameters = "houseNumber", operator = GreaterOrEquals.class, defaultValues = "1010") 
	}
)
public interface FetchingComposedPath {

}
