package com.github.dfr.provider.specification.decoder.type;

import static com.github.dfr.provider.specification.decoder.type.PredicateUtils.computeAttributePath;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.Greater;
import com.github.dfr.filter.FilterParameter;

class SpecGreater<T> implements Greater<Specification<T>>, SpecComparablePredicate {

	@Override
	public Specification<T> decode(FilterParameter metadata, ParameterValueConverter parameterValueConverter, Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(metadata, root);
			Object value = parameterValueConverter.convert(metadata.findSingleValue(), path.getJavaType());
			return toComparablePredicate(criteriaBuilder, path, value, criteriaBuilder::greaterThan, criteriaBuilder::gt);
		};
	}

}
