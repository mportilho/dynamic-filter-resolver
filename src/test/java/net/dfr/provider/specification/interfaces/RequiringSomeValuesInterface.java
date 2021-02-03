package net.dfr.provider.specification.interfaces;

import java.time.LocalDate;

import net.dfr.annotation.Conjunction;
import net.dfr.annotation.Filter;
import net.dfr.annotation.Or;
import net.dfr.operator.type.Equals;
import net.dfr.operator.type.Greater;
import net.dfr.operator.type.GreaterOrEquals;
import net.dfr.operator.type.NotEquals;

//@formatter:off
@Conjunction(
	value = {
		@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
		@Filter(path = "name", parameters = "name", operator = NotEquals.class, targetType = String.class)
	},
	disjunctions = {
		@Or({
			@Filter(path = "status", parameters = "status", operator = Equals.class, targetType = StatusEnum.class)
		}),
		@Or({
			@Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, targetType = LocalDate.class),
			@Filter(path = "height", parameters = "height", operator = Greater.class, targetType = StatusEnum.class)
		})
	}
)
//@formatter:on
public interface RequiringSomeValuesInterface {

}
