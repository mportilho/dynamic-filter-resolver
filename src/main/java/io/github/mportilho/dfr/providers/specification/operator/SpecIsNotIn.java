package io.github.mportilho.dfr.providers.specification.operator;

import org.springframework.data.jpa.domain.Specification;

import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.type.IsNotIn;

class SpecIsNotIn<T> implements IsNotIn<Specification<T>> {

	@SuppressWarnings("rawtypes")
	private static final SpecIsIn IN_STATIC = new SpecIsIn<>();

	@Override
	@SuppressWarnings("unchecked")
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return Specification.not(IN_STATIC.createFilter(filterParameter, filterValueConverter));
	}

}
