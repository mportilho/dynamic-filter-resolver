package net.dfr.provider.specification.interfaces;

import net.dfr.annotation.Conjunction;
import net.dfr.annotation.Filter;
import net.dfr.operator.type.Equals;

//@formatter:off
@Conjunction({
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, defaultValues = "true", targetType = Boolean.class)
})//@formatter:on
public interface RequiringValuesDefaultDataInterface {

}
