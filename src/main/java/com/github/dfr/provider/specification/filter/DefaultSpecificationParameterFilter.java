package com.github.dfr.provider.specification.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.DecoderService;
import com.github.dfr.decoder.FilterDecoder;
import com.github.dfr.decoder.ValueConverter;
import com.github.dfr.filter.ParameterFilterMetadata;

public class DefaultSpecificationParameterFilter<T> implements SpecificationParameterFilter<T> {

	private final DecoderService<Specification<T>> decoderService;
	private final ValueConverter valueConverter;

	public DefaultSpecificationParameterFilter(DecoderService<Specification<T>> decoderService, ValueConverter valueConverter) {
		this.decoderService = decoderService;
		this.valueConverter = valueConverter;
	}

	@Override
	public Specification<T> convertTo(Collection<ParameterFilterMetadata> parameters) {
		Map<String, Object> sharedContext = new HashMap<>();
		Specification<T> rootSpecification = Specification.where((root, query, cb) -> {
			root.fetch("addresses", JoinType.LEFT);
			return null;
		});

		for (ParameterFilterMetadata metadata : parameters) {
			FilterDecoder<Specification<T>> decoder = decoderService.getDecoderFor(metadata.getDecoderClass());
			Specification<T> spec = decoder.decode(metadata, valueConverter, sharedContext);
			if (spec != null) {
				rootSpecification = rootSpecification.and(spec);
			}
		}
		return rootSpecification;
	}

}
