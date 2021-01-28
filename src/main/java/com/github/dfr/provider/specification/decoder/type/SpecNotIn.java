package com.github.dfr.provider.specification.decoder.type;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.NotIn;
import com.github.dfr.filter.FilterParameter;

public class SpecNotIn<T> implements NotIn<Specification<T>> {

	@SuppressWarnings("rawtypes")
	private static final SpecIn IN_STATIC = new SpecIn<>();

	@Override
	@SuppressWarnings("unchecked")
	public Specification<T> decode(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		return Specification.not(IN_STATIC.decode(filterParameter, parameterValueConverter, sharedContext));
	}

}
