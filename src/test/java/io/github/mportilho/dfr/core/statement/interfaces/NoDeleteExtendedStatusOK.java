package io.github.mportilho.dfr.core.statement.interfaces;

import java.io.Serializable;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.Equals;

//@formatter:off
@Conjunction(value = {
	@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
})//@formatter:on
public interface NoDeleteExtendedStatusOK extends NoDelete, Serializable {

}
