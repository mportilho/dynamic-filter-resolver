package com.github.dfr.provider.specification.interfaces;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.annotation.And;
import com.github.dfr.annotation.Filter;
import com.github.dfr.operator.type.Equals;

//@formatter:off
@And({
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
	@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
})//@formatter:on
public interface NoDeleteAndStatusOkSpecification<T> extends Specification<T> {

}
