package com.github.dfr.decoder;

public interface ValueConverter {

	<R> R convert(Object value, Class<R> expectedClass);

}
