package com.github.dfr.provider.specification.operator;

import static com.github.dfr.provider.specification.operator.PredicateUtils.computeAttributePath;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.FilterValueConverter;
import com.github.dfr.operator.type.IsNull;

class SpecIsNull<T> implements IsNull<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(computeAttributePath(filterParameter, root));
	}

}
