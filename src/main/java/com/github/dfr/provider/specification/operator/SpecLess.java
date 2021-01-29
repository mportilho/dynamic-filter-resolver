package com.github.dfr.provider.specification.operator;

import static com.github.dfr.provider.specification.operator.PredicateUtils.computeAttributePath;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.FilterValueConverter;
import com.github.dfr.operator.type.Less;

class SpecLess<T> implements Less<Specification<T>>, SpecComparablePredicate {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object value = filterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.findFormat());
			return toComparablePredicate(criteriaBuilder, path, value, criteriaBuilder::lessThan, criteriaBuilder::lt);
		};
	}

}
