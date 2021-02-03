package net.dfr.provider.specification.interfaces;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.operator.type.Equals;

//@formatter:off
@Conjunction({
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, defaultValues = "true", targetType = Boolean.class)
})//@formatter:on
public interface RequiringValuesDefaultDataInterface {

}
