package io.github.mportilho.apps.webapptest.repository;

import org.springframework.data.jpa.domain.Specification;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.Equals;

@Conjunction(@Filter(path = "deleted", parameters = "deleted", operator = Equals.class, constantValues = "false"))
public interface NoDeletionSpecification<T> extends Specification<T> {

}
