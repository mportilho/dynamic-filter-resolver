package net.dfr.core.statement.annotation;

import java.time.LocalDate;

import net.dfr.core.annotation.Disjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.operator.type.Greater;
import net.dfr.core.operator.type.GreaterOrEquals;
import net.dfr.core.statement.interfaces.StatusEnum;

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