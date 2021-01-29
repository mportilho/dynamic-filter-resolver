package com.github.dfr.provider.specification.operator;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.ParameterValueConverter;
import com.github.dfr.operator.type.EndsWith;

class SpecEndsWith<T> implements EndsWith<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<String> path = PredicateUtils.computeAttributePath(filterParameter, root);
			Object value = parameterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.getFormat());
			return criteriaBuilder.like(path, transformNonNull(value, v -> "%" + v.toString()));
		};
	}

}
