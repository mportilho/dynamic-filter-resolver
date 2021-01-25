package com.github.djs.decoder;

public interface DecoderService<T> {

	FilterDecoder<T> getDecoderFor(Class<? extends FilterDecoder<?>> decoder);

}
