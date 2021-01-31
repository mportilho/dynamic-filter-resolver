package com.github.dfr.provider.specification.interfaces;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.annotation.And;
import com.github.dfr.annotation.Filter;
import com.github.dfr.operator.type.Equals;

//@formatter:off
@And({
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class)
})//@formatter:on
public interface NoDeleteSpecification<T> extends Specification<T> {

}
