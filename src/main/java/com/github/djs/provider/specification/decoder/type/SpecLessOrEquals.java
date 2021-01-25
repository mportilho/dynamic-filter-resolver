package com.github.djs.provider.specification.decoder.type;

import static com.github.djs.provider.specification.decoder.type.PredicateUtils.computeAttributePath;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.djs.decoder.ValueConverter;
import com.github.djs.decoder.type.LessOrEquals;
import com.github.djs.filter.ParameterFilterMetadata;

public class SpecLessOrEquals<T> implements LessOrEquals<Specification<T>>, SpecComparablePredicate {

	@Override
	public Specification<T> decode(ParameterFilterMetadata metadata, ValueConverter valueConverter, Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(metadata, root);
			Object value = valueConverter.convert(metadata.findSingleValue(), path.getJavaType());
			return toComparablePredicate(criteriaBuilder, path, value, criteriaBuilder::lessThanOrEqualTo, criteriaBuilder::le);
		};
	}

}
