package net.dfr.provider.specification.operator;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.filter.FilterParameter;
import net.dfr.operator.FilterValueConverter;
import net.dfr.operator.type.LikeIgnoreCase;

class SpecLikeIgnoreCase<T> implements LikeIgnoreCase<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<String> path = PredicateUtils.computeAttributePath(filterParameter, root);
			Object value = filterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.findFormat());
			return criteriaBuilder.like(criteriaBuilder.upper(path), transformNonNull(value, v -> "%" + v.toString().toUpperCase() + "%"));
		};
	}

}
