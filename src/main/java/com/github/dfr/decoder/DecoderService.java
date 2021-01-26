package com.github.dfr.decoder;

public interface DecoderService<T> {

	FilterDecoder<T> getDecoderFor(Class<? extends FilterDecoder<?>> decoder);

}
