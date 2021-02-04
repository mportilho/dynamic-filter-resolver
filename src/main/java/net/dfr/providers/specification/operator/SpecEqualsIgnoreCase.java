package net.dfr.providers.specification.operator;

import static net.dfr.providers.specification.operator.PredicateUtils.computeAttributePath;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.core.converter.FilterValueConverter;
import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.type.EqualsIgnoreCase;

class SpecEqualsIgnoreCase<T> implements EqualsIgnoreCase<Specification<T>> {

	@Override
	@SuppressWarnings("unchecked")
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object value = filterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.getFormat());
			if (String.class.equals(path.getJavaType())) {
				return criteriaBuilder.equal(criteriaBuilder.upper((Path<String>) path), transformNonNull(value, v -> v.toString().toUpperCase()));
			}
			return criteriaBuilder.equal(path, value);
		};
	}

}
