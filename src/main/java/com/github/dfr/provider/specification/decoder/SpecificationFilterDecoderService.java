package com.github.dfr.provider.specification.decoder;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.FilterDecoderService;

public interface SpecificationFilterDecoderService<T> extends FilterDecoderService<Specification<T>> {

}
