package com.github.dfr.provider.specification.operator.type;

import java.util.function.BiFunction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public interface SpecComparablePredicate {

	@SuppressWarnings({ "unchecked" })
	default Predicate toComparablePredicate(CriteriaBuilder criteriaBuilder, Path<?> path, Object value,
			BiFunction<Expression<? extends Comparable<Object>>, Comparable<Object>, Predicate> comparablePredicateFunction,
			BiFunction<Expression<Number>, Number, Predicate> numberPredicateFunction) {
		if (value != null && Number.class.isAssignableFrom(value.getClass())) {
			return numberPredicateFunction.apply((Expression<Number>) path, (Number) value);
		}
		return comparablePredicateFunction.apply((Expression<? extends Comparable<Object>>) path, (Comparable<Object>) value);
	}

}
