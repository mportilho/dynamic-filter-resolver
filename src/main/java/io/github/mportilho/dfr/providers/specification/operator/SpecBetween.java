package io.github.mportilho.dfr.providers.specification.operator;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.type.Between;

class SpecBetween<T> implements Between<Specification<T>> {

	@Override
	@SuppressWarnings("unchecked")
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<Comparable<Object>> path = PredicateUtils.computeAttributePath(filterParameter, root);
			Comparable<Object> lowerValue = (Comparable<Object>) filterParameter.getValues()[0];
			Comparable<Object> upperValue = (Comparable<Object>) filterParameter.getValues()[1];

			lowerValue = filterValueConverter.convert(lowerValue, path.getJavaType(), filterParameter.getFormat());
			upperValue = filterValueConverter.convert(upperValue, path.getJavaType(), filterParameter.getFormat());
			return criteriaBuilder.between(path, lowerValue, upperValue);
		};
	}

}