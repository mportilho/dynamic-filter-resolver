package com.github.dfr.provider.specification.decoder;

import com.github.dfr.decoder.ValueConverter;

public class SpecificationValueConverter implements ValueConverter {

	@Override
	public <R> R convert(Object value, Class<R> expectedClass) {
		return (R) value;
	}

}
