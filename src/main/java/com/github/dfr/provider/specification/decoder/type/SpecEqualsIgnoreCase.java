package com.github.dfr.provider.specification.decoder.type;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.EqualsIgnoreCase;
import com.github.dfr.filter.FilterParameter;

public class SpecEqualsIgnoreCase<T> implements EqualsIgnoreCase<Specification<T>> {

	@Override
	public Specification<T> decode(FilterParameter filterParameter, ParameterValueConverter parameterValueConverter,
			Map<String, Object> sharedContext) {
		// TODO Auto-generated method stub
		return null;
	}

}
