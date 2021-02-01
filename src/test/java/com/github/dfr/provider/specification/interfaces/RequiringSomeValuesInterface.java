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
