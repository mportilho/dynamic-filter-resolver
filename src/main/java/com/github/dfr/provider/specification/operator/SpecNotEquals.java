package com.github.dfr.provider.specification.operator;

import static com.github.dfr.provider.specification.operator.PredicateUtils.computeAttributePath;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.FilterValueConverter;
import com.github.dfr.operator.type.NotEquals;

class SpecNotEquals<T> implements NotEquals<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object value = filterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.findFormat());
			return criteriaBuilder.notEqual(path, value);
		};
	}

}
