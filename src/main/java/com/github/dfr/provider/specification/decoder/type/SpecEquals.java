package com.github.dfr.provider.specification.decoder.type;

import static com.github.dfr.provider.specification.decoder.type.PredicateUtils.computeAttributePath;

import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterConverter;
import com.github.dfr.decoder.type.Equals;
import com.github.dfr.filter.ParameterFilterMetadata;

class SpecEquals<T> implements Equals<Specification<T>> {

	@Override
	public Specification<T> decode(ParameterFilterMetadata metadata, ParameterConverter parameterConverter, Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(metadata, root);
			Object value = parameterConverter.convert(metadata.findSingleValue(), path.getJavaType());
			return criteriaBuilder.equal(path, value);
		};
	}

}
