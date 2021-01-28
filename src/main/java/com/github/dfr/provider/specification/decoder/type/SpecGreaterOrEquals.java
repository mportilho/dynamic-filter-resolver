package com.github.dfr.provider.specification.decoder.type;

import static com.github.dfr.provider.specification.decoder.type.PredicateUtils.computeAttributePath;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.GreaterOrEquals;
import com.github.dfr.filter.FilterParameter;

class SpecGreaterOrEquals<T> implements GreaterOrEquals<Specification<T>>, SpecComparablePredicate {

	@Override
	public Specification<T> decode(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object value = parameterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.getFormat());
			return toComparablePredicate(criteriaBuilder, path, value, criteriaBuilder::greaterThanOrEqualTo, criteriaBuilder::ge);
		};
	}

}
