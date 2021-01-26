package com.github.dfr.provider.specification.filter;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.ParameterFilter;

public interface SpecificationParameterFilter<T> extends ParameterFilter<Specification<T>> {

}
