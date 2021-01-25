package com.github.djs.provider.specification.decoder;

import org.springframework.data.jpa.domain.Specification;

import com.github.djs.decoder.DecoderService;

public interface SpecificationDecoderService<T> extends DecoderService<Specification<T>> {

}
