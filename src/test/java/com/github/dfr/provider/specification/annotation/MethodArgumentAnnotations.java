package com.github.dfr.provider.specification.annotation;

import java.time.LocalDate;

import com.github.dfr.annotation.Disjunction;
import com.github.dfr.annotation.Filter;
import com.github.dfr.operator.type.Greater;
import com.github.dfr.operator.type.GreaterOrEquals;
import com.github.dfr.provider.specification.interfaces.StatusEnum;

/**
 * Simulates parameter annotations
 * 
 * @author Marcelo Portilho
 *
 */
@Disjunction({
		@Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, constantValues = "12/12/2012", targetType = LocalDate.class),
		@Filter(path = "height", parameters = "height", operator = Greater.class, constantValues = "170", targetType = StatusEnum.class) })
public interface MethodArgumentAnnotations {

}
