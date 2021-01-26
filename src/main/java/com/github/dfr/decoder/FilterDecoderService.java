package com.github.dfr.decoder;

public interface FilterDecoderService<T> {

	FilterDecoder<T> getFilterDecoderFor(Class<? extends FilterDecoder<?>> decoder);

}
