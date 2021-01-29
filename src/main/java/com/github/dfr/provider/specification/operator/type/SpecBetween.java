package com.github.dfr.provider.specification.operator.type;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.ParameterValueConverter;
import com.github.dfr.operator.type.Between;

class SpecBetween<T> implements Between<Specification<T>> {

	@Override
	@SuppressWarnings("unchecked")
	public Specification<T> createFilter(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<Comparable<Object>> path = PredicateUtils.computeAttributePath(filterParameter, root);
			Comparable<Object> lowerValue = (Comparable<Object>) filterParameter.getValues()[0];
			Comparable<Object> upperValue = (Comparable<Object>) filterParameter.getValues()[1];

			lowerValue = parameterValueConverter.convert(lowerValue, path.getJavaType(), filterParameter.getFormat()[0]);
			upperValue = parameterValueConverter.convert(upperValue, path.getJavaType(), filterParameter.getFormat()[1]);

			return criteriaBuilder.between(path, lowerValue, upperValue);
		};
	}

}
