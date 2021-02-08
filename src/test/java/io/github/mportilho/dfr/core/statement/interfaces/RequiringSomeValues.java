package io.github.mportilho.dfr.core.statement.interfaces;

import java.time.LocalDate;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.annotation.Statement;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.operator.type.Greater;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;
import io.github.mportilho.dfr.core.operator.type.NotEquals;

//@formatter:off
@Conjunction(
	value = {
		@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
		@Filter(path = "name", parameters = "name", operator = NotEquals.class, targetType = String.class)
	},
	disjunctions = {
		@Statement({
			@Filter(path = "status", parameters = "status", operator = Equals.class, targetType = StatusEnum.class)
		}),
		@Statement({
			@Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, targetType = LocalDate.class),
			@Filter(path = "height", parameters = "height", operator = Greater.class, targetType = StatusEnum.class)
		})
	}
)
//@formatter:on
public interface RequiringSomeValues {

}
