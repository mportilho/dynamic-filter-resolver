package io.github.mportilho.dfr.core.statement.interfaces;

import java.time.LocalDate;

import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.annotation.Statement;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.operator.type.Greater;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;
import io.github.mportilho.dfr.core.operator.type.NotEquals;

//@formatter:off
@Disjunction(
	negate = "true",
	value = {
		@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
		@Filter(path = "name", parameters = "name", operator = NotEquals.class, constantValues = "Blanka", targetType = String.class)
	},
	conjunctions = {
		@Statement(
			negate = "true",
			value = {
				@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
		}),
		@Statement(
			negate = "true",
			value = {
				@Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, constantValues = "12/12/2012", targetType = LocalDate.class),
				@Filter(path = "height", parameters = "height", operator = Greater.class, constantValues = "170", targetType = StatusEnum.class)
		})
	}
)
//@formatter:on
public interface FullyRequiringDisjunctionNegatingAll {

}
