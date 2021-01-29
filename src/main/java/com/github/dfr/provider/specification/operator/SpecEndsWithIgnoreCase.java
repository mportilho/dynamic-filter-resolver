package com.github.dfr.provider.specification.operator;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.FilterValueConverter;
import com.github.dfr.operator.type.EndsWithIgnoreCase;

class SpecEndsWithIgnoreCase<T> implements EndsWithIgnoreCase<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<String> path = PredicateUtils.computeAttributePath(filterParameter, root);
			Object value = filterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.findFormat());
			return criteriaBuilder.like(criteriaBuilder.upper(path), transformNonNull(value, v -> "%" + v.toString().toUpperCase()));
		};
	}

}
