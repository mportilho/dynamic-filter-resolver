package com.github.djs.decoder;

public interface ValueConverter {

	<R> R convert(Object value, Class<R> expectedClass);

}
