package net.dfr.provider.specification.interfaces;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.operator.type.Equals;

//@formatter:off
@Conjunction({
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
	@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
})//@formatter:on
public interface NoDeleteAndStatusOkSpecification<T> extends Specification<T> {

}