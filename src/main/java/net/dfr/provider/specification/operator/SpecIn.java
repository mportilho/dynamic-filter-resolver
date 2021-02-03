package net.dfr.provider.specification.operator;

import static net.dfr.provider.specification.operator.PredicateUtils.computeAttributePath;

import java.util.Collection;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.filter.FilterParameter;
import net.dfr.operator.FilterValueConverter;
import net.dfr.operator.type.In;

class SpecIn<T> implements In<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object value = filterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.findFormat());
			if (value == null || !value.getClass().isArray() || !Collection.class.isAssignableFrom(value.getClass())) {
				throw new IllegalArgumentException("No list of elements found for 'in' operation");
			}
			return path.in(value);
		};
	}

}
