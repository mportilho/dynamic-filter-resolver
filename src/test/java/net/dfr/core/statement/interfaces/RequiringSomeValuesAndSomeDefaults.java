package net.dfr.core.statement.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;

import net.dfr.core.annotation.Disjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.annotation.Statement;
import net.dfr.core.operator.type.Equals;
import net.dfr.core.operator.type.Greater;
import net.dfr.core.operator.type.GreaterOrEquals;
import net.dfr.core.operator.type.LessOrEquals;
import net.dfr.core.operator.type.NotEquals;

//@formatter:off
@Disjunction(
	value = {
		@Filter(path = "deleted", parameters = "delete", operator = Equals.class, defaultValues = "false", targetType = Boolean.class),
		@Filter(path = "name", parameters = "name", operator = NotEquals.class, targetType = String.class)
	},
	conjunctions = {
		@Statement({
			@Filter(path = "status", parameters = "status", operator = Equals.class, targetType = StatusEnum.class)
		}),
		@Statement({
			@Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, defaultValues = "12/12/2012", targetType = LocalDate.class),
			@Filter(path = "height", parameters = "height", operator = Greater.class, targetType = StatusEnum.class)
		}),
		@Statement({
			@Filter(path = "student", parameters = "student", operator = Equals.class, constantValues = "true", targetType = Boolean.class),
			@Filter(path = "weight", parameters = "weight", operator = LessOrEquals.class, targetType = BigDecimal.class)
		})
	}
)
//@formatter:on
public interface RequiringSomeValuesAndSomeDefaults {

}
