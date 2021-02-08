package io.github.mportilho.dfr.providers.specification.operator;

import static io.github.mportilho.dfr.providers.specification.operator.PredicateUtils.computeAttributePath;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;

class SpecGreaterOrEquals<T> implements GreaterOrEquals<Specification<T>>, SpecComparablePredicate {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object value = filterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.getFormat());
			return toComparablePredicate(criteriaBuilder, path, value, criteriaBuilder::greaterThanOrEqualTo, criteriaBuilder::ge);
		};
	}

}
