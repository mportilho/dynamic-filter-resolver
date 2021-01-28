package com.github.dfr.provider.specification.decoder.type;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.FilterDecoder;
import com.github.dfr.decoder.FilterDecoderService;
import com.github.dfr.decoder.type.Equals;
import com.github.dfr.decoder.type.Greater;
import com.github.dfr.decoder.type.GreaterOrEquals;

public class DefaultSpecificationDecoderService<T> implements FilterDecoderService<Specification<T>> {

	@SuppressWarnings("rawtypes")
	private Map<Class<? extends FilterDecoder>, FilterDecoder<Specification<T>>> decoders;

	public DefaultSpecificationDecoderService() {
		decoders = new HashMap<>();
		decoders.put(Equals.class, new SpecEquals<T>());
		decoders.put(Greater.class, new SpecGreater<T>());
		decoders.put(GreaterOrEquals.class, new SpecGreaterOrEquals<T>());
	}

	@Override
	public FilterDecoder<Specification<T>> getFilterDecoderFor(Class<? extends FilterDecoder<?>> decoder) {
		return decoders.get(decoder);
	}

}
