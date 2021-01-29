package com.github.dfr.provider.specification.operator.type;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.ParameterValueConverter;
import com.github.dfr.operator.type.NotIn;

public class SpecNotIn<T> implements NotIn<Specification<T>> {

	@SuppressWarnings("rawtypes")
	private static final SpecIn IN_STATIC = new SpecIn<>();

	@Override
	@SuppressWarnings("unchecked")
	public Specification<T> createFilter(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return Specification.not(IN_STATIC.createFilter(filterParameter, parameterValueConverter, sharedContext));
	}

}
