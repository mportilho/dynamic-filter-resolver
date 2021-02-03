package net.dfr.provider.specification.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.annotation.Or;
import net.dfr.core.operator.type.Equals;
import net.dfr.core.operator.type.Greater;
import net.dfr.core.operator.type.GreaterOrEquals;
import net.dfr.core.operator.type.LessOrEquals;
import net.dfr.core.operator.type.NotEquals;

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
		}),
		@Or({
			@Filter(path = "weight", parameters = "weight", operator = LessOrEquals.class, constantValues = "80", targetType = BigDecimal.class),
			@Filter(path = "document", parameters = "document", operator = Greater.class, targetType = String.class)
		})
	}
)
//@formatter:on
public interface RequiringSomeMoreValuesInterface {

}
