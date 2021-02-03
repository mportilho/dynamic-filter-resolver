package net.dfr.provider.specification.interfaces;

import java.time.LocalDate;

import net.dfr.annotation.And;
import net.dfr.annotation.Disjunction;
import net.dfr.annotation.Filter;
import net.dfr.operator.type.Equals;
import net.dfr.operator.type.Greater;
import net.dfr.operator.type.GreaterOrEquals;
import net.dfr.operator.type.NotEquals;

//@formatter:off
@Disjunction(
	value = {
		@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
		@Filter(path = "name", parameters = "name", operator = NotEquals.class, constantValues = "Blanka", targetType = String.class)
	},
	conjunctions = {
		@And({
			@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
		}),
		@And({
			@Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, constantValues = "12/12/2012", targetType = LocalDate.class),
			@Filter(path = "height", parameters = "height", operator = Greater.class, constantValues = "170", targetType = StatusEnum.class)
		})
	}
)
//@formatter:on
public interface FullyRequiringDisjunctionInterface {

}
