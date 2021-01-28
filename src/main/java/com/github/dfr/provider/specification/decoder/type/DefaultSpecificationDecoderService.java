package com.github.dfr.provider.specification.decoder.type;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.FilterDecoder;
import com.github.dfr.decoder.FilterDecoderService;
import com.github.dfr.decoder.type.Between;
import com.github.dfr.decoder.type.EndsWith;
import com.github.dfr.decoder.type.EndsWithIgnoreCase;
import com.github.dfr.decoder.type.Equals;
import com.github.dfr.decoder.type.EqualsIgnoreCase;
import com.github.dfr.decoder.type.Greater;
import com.github.dfr.decoder.type.GreaterOrEquals;
import com.github.dfr.decoder.type.In;
import com.github.dfr.decoder.type.IsNotNull;
import com.github.dfr.decoder.type.IsNull;
import com.github.dfr.decoder.type.Less;
import com.github.dfr.decoder.type.LessOrEquals;
import com.github.dfr.decoder.type.Like;
import com.github.dfr.decoder.type.LikeIgnoreCase;
import com.github.dfr.decoder.type.NotEquals;
import com.github.dfr.decoder.type.NotEqualsIgnoreCase;
import com.github.dfr.decoder.type.NotIn;
import com.github.dfr.decoder.type.NotLike;
import com.github.dfr.decoder.type.NotLikeIgnoreCase;
import com.github.dfr.decoder.type.StartsWith;
import com.github.dfr.decoder.type.StartsWithIgnoreCase;

public class DefaultSpecificationDecoderService<T> implements FilterDecoderService<Specification<T>> {

	@SuppressWarnings("rawtypes")
	private Map<Class<? extends FilterDecoder>, FilterDecoder<Specification<T>>> decoders;

	public DefaultSpecificationDecoderService() {
		decoders = new HashMap<>();
		decoders.put(Between.class, new SpecBetween<T>());
		decoders.put(EndsWith.class, new SpecEndsWith<T>());
		decoders.put(EndsWithIgnoreCase.class, new SpecEndsWithIgnoreCase<T>());
		decoders.put(Equals.class, new SpecEquals<T>());
		decoders.put(EqualsIgnoreCase.class, new SpecEqualsIgnoreCase<T>());
		decoders.put(Greater.class, new SpecGreater<T>());
		decoders.put(GreaterOrEquals.class, new SpecGreaterOrEquals<T>());
		decoders.put(In.class, new SpecIn<T>());
		decoders.put(IsNotNull.class, new SpecIsNotNull<T>());
		decoders.put(IsNull.class, new SpecIsNull<T>());
		decoders.put(Less.class, new SpecLess<T>());
		decoders.put(LessOrEquals.class, new SpecLessOrEquals<T>());
		decoders.put(Like.class, new SpecLike<T>());
		decoders.put(LikeIgnoreCase.class, new SpecLikeIgnoreCase<T>());
		decoders.put(NotEquals.class, new SpecNotEquals<T>());
		decoders.put(NotEqualsIgnoreCase.class, new SpecNotEqualsIgnoreCase<T>());
		decoders.put(NotIn.class, new SpecNotIn<T>());
		decoders.put(NotLike.class, new SpecNotLike<T>());
		decoders.put(NotLikeIgnoreCase.class, new SpecNotLikeIgnoreCase<T>());
		decoders.put(StartsWith.class, new SpecStartsWith<T>());
		decoders.put(StartsWithIgnoreCase.class, new SpecStartsWithIgnoreCase<T>());
	}

	@Override
	public FilterDecoder<Specification<T>> getFilterDecoderFor(Class<? extends FilterDecoder<?>> decoder) {
		return decoders.get(decoder);
	}

}
