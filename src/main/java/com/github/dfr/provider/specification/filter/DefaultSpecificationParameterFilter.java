package com.github.dfr.provider.specification.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.FilterDecoderService;
import com.github.dfr.decoder.FilterDecoder;
import com.github.dfr.decoder.ParameterConverter;
import com.github.dfr.filter.ParameterFilterMetadata;

public class DefaultSpecificationParameterFilter<T> implements SpecificationParameterFilter<T> {

	private final FilterDecoderService<Specification<T>> filterDecoderService;
	private final ParameterConverter parameterConverter;

	public DefaultSpecificationParameterFilter(FilterDecoderService<Specification<T>> decoderService, ParameterConverter parameterConverter) {
		this.filterDecoderService = decoderService;
		this.parameterConverter = parameterConverter;
	}

	@Override
	public Specification<T> convertTo(Collection<ParameterFilterMetadata> parameters) {
		Map<String, Object> sharedContext = new HashMap<>();
		Specification<T> rootSpecification = Specification.where((root, query, cb) -> {
			root.fetch("addresses", JoinType.INNER);
			root.fetch("phones", JoinType.INNER);
			return null;
		});

		for (ParameterFilterMetadata metadata : parameters) {
			FilterDecoder<Specification<T>> decoder = filterDecoderService.getFilterDecoderFor(metadata.getDecoderClass());
			Specification<T> spec = decoder.decode(metadata, parameterConverter, sharedContext);
			if (spec != null) {
				rootSpecification = rootSpecification.and(spec);
			}
		}
		return rootSpecification;
	}

}
