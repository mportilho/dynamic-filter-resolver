package io.github.mportilho.dfr.core.statement.annotation;

import java.time.LocalDate;

import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.Greater;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;
import io.github.mportilho.dfr.core.statement.interfaces.StatusEnum;

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
