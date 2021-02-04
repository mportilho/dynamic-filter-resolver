package net.dfr.core.statement.interfaces.queries;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.operator.type.IsIn;
import net.dfr.core.operator.type.IsNotIn;
import net.dfr.core.operator.type.IsNotNull;
import net.dfr.core.operator.type.IsNull;

@Conjunction(value = { 
		@Filter(path = "addresses.number", parameters = "houseNumber", operator = IsNull.class),
		@Filter(path = "name", parameters = "clientName", operator = IsNotNull.class),
		@Filter(path = "height", parameters = "height", operator = IsIn.class, defaultValues = { "170", "180", "190" }),
		@Filter(path = "addresses.number", parameters = "houseNumber", operator = IsNotIn.class, defaultValues = { "1010", "1020", "1030" }) 
	}
)
public interface OtherComparisonOperations {

}
