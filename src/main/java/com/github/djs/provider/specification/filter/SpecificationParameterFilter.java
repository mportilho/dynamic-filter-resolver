package com.github.djs.provider.specification.filter;

import org.springframework.data.jpa.domain.Specification;

import com.github.djs.filter.ParameterFilter;

public interface SpecificationParameterFilter<T> extends ParameterFilter<Specification<T>> {

}
