package com.github.dfr.provider.specification.decoder.type;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.NotLikeIgnoreCase;
import com.github.dfr.filter.FilterParameter;

public class SpecNotLikeIgnoreCase<T> implements NotLikeIgnoreCase<Specification<T>> {

	@Override
	public Specification<T> decode(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<String> path = PredicateUtils.computeAttributePath(filterParameter, root);
			Object value = parameterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.getFormat());
			return criteriaBuilder.notLike(criteriaBuilder.upper(path), transformNonNull(value, v -> "%" + v.toString().toUpperCase() + "%"));
		};
	}

}