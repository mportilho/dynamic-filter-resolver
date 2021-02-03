package net.dfr.provider.specification.annotation;

import java.time.LocalDate;

import net.dfr.annotation.Disjunction;
import net.dfr.annotation.Filter;
import net.dfr.operator.type.Greater;
import net.dfr.operator.type.GreaterOrEquals;
import net.dfr.provider.specification.interfaces.StatusEnum;

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
