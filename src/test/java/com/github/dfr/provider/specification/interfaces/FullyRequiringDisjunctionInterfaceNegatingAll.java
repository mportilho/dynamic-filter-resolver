package com.github.dfr.provider.specification.interfaces;

import java.time.LocalDate;

import com.github.dfr.annotation.And;
import com.github.dfr.annotation.Disjunction;
import com.github.dfr.annotation.Filter;
import com.github.dfr.operator.type.Equals;
import com.github.dfr.operator.type.Greater;
import com.github.dfr.operator.type.GreaterOrEquals;
import com.github.dfr.operator.type.NotEquals;

//@formatter:off
@Disjunction(
	negate = "true",
	value = {
		@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
		@Filter(path = "name", parameters = "name", operator = NotEquals.class, constantValues = "Blanka", targetType = String.class)
	},
	conjunctions = {
		@And(
			negate = "true",
			value = {
				@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
		}),
		@And(
			negate = "true",
			value = {
				@Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, constantValues = "12/12/2012", targetType = LocalDate.class),
				@Filter(path = "height", parameters = "height", operator = Greater.class, constantValues = "170", targetType = StatusEnum.class)
		})
	}
)
//@formatter:on
public interface FullyRequiringDisjunctionInterfaceNegatingAll {

}
