package com.github.dfr.provider.specification.operator.type;

import static com.github.dfr.provider.specification.operator.type.PredicateUtils.computeAttributePath;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.ParameterValueConverter;
import com.github.dfr.operator.type.IsNotNull;

class SpecIsNotNull<T> implements IsNotNull<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(computeAttributePath(filterParameter, root));
	}

}
