package net.dfr.provider.specification.operator;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.filter.FilterParameter;
import net.dfr.operator.FilterValueConverter;
import net.dfr.operator.type.Between;

class SpecBetween<T> implements Between<Specification<T>> {

	@Override
	@SuppressWarnings("unchecked")
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<Comparable<Object>> path = PredicateUtils.computeAttributePath(filterParameter, root);
			Comparable<Object> lowerValue = (Comparable<Object>) filterParameter.getValues()[0];
			Comparable<Object> upperValue = (Comparable<Object>) filterParameter.getValues()[1];

			String lowerValueFormat = null;
			String upperValueFormat = null;
			if (filterParameter.getFormats() != null || filterParameter.getFormats().length > 0) {
				lowerValueFormat = filterParameter.getFormats()[0];
				upperValueFormat = filterParameter.getFormats().length == 2 ? filterParameter.getFormats()[1] : upperValueFormat;
			}

			lowerValue = filterValueConverter.convert(lowerValue, path.getJavaType(), lowerValueFormat);
			upperValue = filterValueConverter.convert(upperValue, path.getJavaType(), upperValueFormat);

			return criteriaBuilder.between(path, lowerValue, upperValue);
		};
	}

}
