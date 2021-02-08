package io.github.mportilho.apptests.app.queries;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.Between;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.operator.type.Greater;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;
import io.github.mportilho.dfr.core.operator.type.Less;
import io.github.mportilho.dfr.core.operator.type.LessOrEquals;
import io.github.mportilho.dfr.core.operator.type.NotEquals;

@Conjunction(value = { 
		@Filter(path = "addresses.number", parameters = "houseNumber", operator = Equals.class, defaultValues = "123"),
		@Filter(path = "name", parameters = "clientName", operator = NotEquals.class, defaultValues = "Anonymous"),
		@Filter(path = "height", parameters = "height", operator = Greater.class, defaultValues = "170"),
		@Filter(path = "addresses.number", parameters = "houseNumber", operator = GreaterOrEquals.class, defaultValues = "1010"), 
		@Filter(path = "weight", parameters = "weight", operator = Less.class, defaultValues = "70"), 
		@Filter(path = "registerDate", parameters = "registerDate", operator = LessOrEquals.class, defaultValues = "05/03/2018"), 
		@Filter(path = "birthday", parameters = { "startDate", "endDate" }, operator = Between.class, defaultValues = { "01/01/1980", "01/01/2000" }) 
	}
)
public interface ComparisonOperationsQueryInterface {

}
