package com.github.dfr.provider.specification.decoder.type;

import static com.github.dfr.provider.specification.decoder.type.PredicateUtils.computeAttributePath;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.Equals;
import com.github.dfr.filter.FilterParameter;

public class SpecLess<T> implements Equals<Specification<T>>, SpecComparablePredicate {

	@Override
	public Specification<T> decode(FilterParameter metadata, ParameterValueConverter parameterValueConverter, Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(metadata, root);
			Object value = parameterValueConverter.convert(metadata.findSingleValue(), path.getJavaType());
			return toComparablePredicate(criteriaBuilder, path, value, criteriaBuilder::lessThan, criteriaBuilder::lt);
		};
	}

}
