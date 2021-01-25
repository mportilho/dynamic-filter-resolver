package com.github.djs.provider.specification.decoder;

import com.github.djs.decoder.ValueConverter;

public class SpecificationValueConverter implements ValueConverter {

	@Override
	public <R> R convert(Object value, Class<R> expectedClass) {
		return (R) value;
	}

}
