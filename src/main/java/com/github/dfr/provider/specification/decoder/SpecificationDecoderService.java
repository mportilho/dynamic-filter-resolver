package com.github.dfr.provider.specification.decoder;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.decoder.DecoderService;

public interface SpecificationDecoderService<T> extends DecoderService<Specification<T>> {

}
