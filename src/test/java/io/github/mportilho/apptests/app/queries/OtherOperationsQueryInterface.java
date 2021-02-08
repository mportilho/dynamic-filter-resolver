package io.github.mportilho.apptests.app.queries;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.IsIn;
import io.github.mportilho.dfr.core.operator.type.IsNotIn;
import io.github.mportilho.dfr.core.operator.type.IsNotNull;
import io.github.mportilho.dfr.core.operator.type.IsNull;

@Conjunction(value = { 
		@Filter(path = "addresses.number", parameters = "houseNumber", operator = IsNull.class),
		@Filter(path = "name", parameters = "clientName", operator = IsNotNull.class),
		@Filter(path = "height", parameters = "height", operator = IsIn.class, defaultValues = { "170", "180", "190" }),
		@Filter(path = "addresses.number", parameters = "houseNumber", operator = IsNotIn.class, defaultValues = { "1010", "1020", "1030" }) 
	}
)
public interface OtherOperationsQueryInterface {

}
