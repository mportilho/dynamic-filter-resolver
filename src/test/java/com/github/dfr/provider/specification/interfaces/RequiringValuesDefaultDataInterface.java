package com.github.dfr.provider.specification.interfaces;

import com.github.dfr.annotation.Conjunction;
import com.github.dfr.annotation.Filter;
import com.github.dfr.operator.type.Equals;

//@formatter:off
@Conjunction({
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, defaultValues = "true", targetType = Boolean.class)
})//@formatter:on
public interface RequiringValuesDefaultDataInterface {

}
