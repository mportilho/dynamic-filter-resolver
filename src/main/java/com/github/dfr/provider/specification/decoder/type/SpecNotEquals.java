package com.github.dfr.provider.specification.decoder.type;

import static com.github.dfr.provider.specification.decoder.type.PredicateUtils.computeAttributePath;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.NotEquals;
import com.github.dfr.filter.FilterParameter;

class SpecNotEquals<T> implements NotEquals<Specification<T>> {

	@Override
	public Specification<T> decode(FilterParameter metadata, ParameterValueConverter parameterValueConverter, Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(metadata, root);
			Object value = parameterValueConverter.convert(metadata.findSingleValue(), path.getJavaType());
			return criteriaBuilder.notEqual(path, value);
		};
	}

}
