package net.dfr.providers.specification.operator;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.core.converter.FilterValueConverter;
import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.type.NotLike;

class SpecNotLike<T> implements NotLike<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<String> path = PredicateUtils.computeAttributePath(filterParameter, root);
			Object value = filterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.getFormat());
			return criteriaBuilder.notLike(path, transformNonNull(value, v -> "%" + v.toString() + "%"));
		};
	}

}
