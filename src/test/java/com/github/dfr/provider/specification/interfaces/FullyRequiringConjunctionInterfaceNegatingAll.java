package com.github.dfr.provider.specification.interfaces;

import java.time.LocalDate;

import com.github.dfr.annotation.Conjunction;
import com.github.dfr.annotation.Filter;
import com.github.dfr.annotation.Or;
import com.github.dfr.operator.type.Equals;
import com.github.dfr.operator.type.Greater;
import com.github.dfr.operator.type.GreaterOrEquals;
import com.github.dfr.operator.type.NotEquals;

//@formatter:off
@Conjunction(
	negate = "true",
	value = {
		@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
		@Filter(path = "name", parameters = "name", operator = NotEquals.class, constantValues = "Blanka", targetType = String.class)
	},
	disjunctions = {
		@Or(
			negate = "true",
			value = {
				@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
			}
		),
		@Or(
			negate = "true",
			value = {
				@Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, constantValues = "12/12/2012", targetType = LocalDate.class),
				@Filter(path = "height", parameters = "height", operator = Greater.class, constantValues = "170", targetType = StatusEnum.class)
			}
		)
	}
)
//@formatter:on
public interface FullyRequiringConjunctionInterfaceNegatingAll {

}
