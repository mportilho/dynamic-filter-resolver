package com.github.dfr.provider.specification.interfaces;

import java.io.Serializable;

import com.github.dfr.annotation.Conjunction;
import com.github.dfr.annotation.Filter;
import com.github.dfr.operator.type.Equals;

//@formatter:off
@Conjunction({
	@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
})//@formatter:on
public interface NoDeleteExtendedStatusOKSpecification<T> extends NoDeleteSpecification<T>, Serializable {

}
