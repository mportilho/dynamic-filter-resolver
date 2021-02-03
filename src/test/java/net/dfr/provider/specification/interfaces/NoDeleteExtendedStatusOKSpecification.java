package net.dfr.provider.specification.interfaces;

import java.io.Serializable;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.operator.type.Equals;

//@formatter:off
@Conjunction({
	@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
})//@formatter:on
public interface NoDeleteExtendedStatusOKSpecification<T> extends NoDeleteSpecification<T>, Serializable {

}
