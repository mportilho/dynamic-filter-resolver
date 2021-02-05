package net.apps.webapptest.repository;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.operator.type.Equals;

@Conjunction(@Filter(path = "deleted", parameters = "deleted", operator = Equals.class, constantValues = "false"))
public interface NoDeletionSpecification<T> extends Specification<T> {

}
