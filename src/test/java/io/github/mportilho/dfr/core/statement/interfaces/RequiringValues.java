package io.github.mportilho.dfr.core.statement.interfaces;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.Equals;

//@formatter:off
@Conjunction({
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, targetType = Boolean.class)
})//@formatter:on
public interface RequiringValues {

}
