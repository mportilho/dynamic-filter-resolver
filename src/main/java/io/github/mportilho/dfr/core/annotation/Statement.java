package io.github.mportilho.dfr.core.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

@Documented
@Retention(RUNTIME)
public @interface Statement {

	Filter[] value();

	String negate() default "false";

}
