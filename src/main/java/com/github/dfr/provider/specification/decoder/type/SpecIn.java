package com.github.dfr.provider.specification.decoder.type;

import static com.github.dfr.provider.specification.decoder.type.PredicateUtils.computeAttributePath;

import java.util.Collection;
import java.util.Map;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.In;
import com.github.dfr.filter.FilterParameter;

public class SpecIn<T> implements In<Specification<T>> {

	@Override
	public Specification<T> decode(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object value = parameterValueConverter.convert(filterParameter.findValue(), path.getJavaType(), filterParameter.getFormat());
			if (value == null || !value.getClass().isArray() || !Collection.class.isAssignableFrom(value.getClass())) {
				throw new IllegalArgumentException("No list of elements found for 'in' operation");
			}
			return path.in(value);
		};
	}

}
