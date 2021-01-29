package com.github.dfr.provider.specification.operator.type;

import static com.github.dfr.provider.specification.operator.type.PredicateUtils.computeAttributePath;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.ParameterValueConverter;
import com.github.dfr.operator.type.GreaterOrEquals;

class SpecGreaterOrEquals<T> implements GreaterOrEquals<Specification<T>>, SpecComparablePredicate {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object value = parameterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.getFormat());
			return toComparablePredicate(criteriaBuilder, path, value, criteriaBuilder::greaterThanOrEqualTo, criteriaBuilder::ge);
		};
	}

}
