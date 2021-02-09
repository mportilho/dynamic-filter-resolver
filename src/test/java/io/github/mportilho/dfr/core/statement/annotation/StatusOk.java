package io.github.mportilho.dfr.core.statement.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.statement.interfaces.StatusEnum;

@Documented
@Retention(RUNTIME)
@Target({ PARAMETER, TYPE })
//@formatter:off
@Conjunction(value = {
	@Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
})//@formatter:on
public @interface StatusOk {

}
