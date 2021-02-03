package net.dfr.provider.specification.operator;

import static net.dfr.provider.specification.operator.PredicateUtils.computeAttributePath;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.filter.FilterParameter;
import net.dfr.operator.FilterValueConverter;
import net.dfr.operator.type.IsNull;

class SpecIsNull<T> implements IsNull<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(computeAttributePath(filterParameter, root));
	}

}
