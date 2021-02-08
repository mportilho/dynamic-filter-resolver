package io.github.mportilho.dfr.providers.specification.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.persistence.criteria.JoinType;

@Documented
@Retention(RUNTIME)
@Target({ PARAMETER, TYPE })
@Repeatable(Fetches.class)
public @interface Fetching {

	String[] value();

	JoinType joinType() default JoinType.LEFT;

}
