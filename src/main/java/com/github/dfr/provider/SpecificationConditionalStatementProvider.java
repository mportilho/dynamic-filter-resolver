package com.github.dfr.provider;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringValueResolver;

import com.github.dfr.filter.AbstractConditionalStatementProvider;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.provider.specification.annotation.Fetch;
import com.github.dfr.provider.specification.annotation.Fetches;

public class SpecificationConditionalStatementProvider<T extends Specification<?>> extends AbstractConditionalStatementProvider<T> {

	public SpecificationConditionalStatementProvider(StringValueResolver stringValueResolver) {
		super(stringValueResolver);
	}

	@Override
	public FilterParameter decorateFilterParameter(FilterParameter filterParameter, Map<Object, Object[]> parametersMap) {
		Fetches[] fetchesArray = (Fetches[]) parametersMap.get(Fetches.class);
		if (fetchesArray != null && fetchesArray.length > 0) {
			Fetches fetches = fetchesArray[0];
			Map<String, Fetch> fetchingMap = new HashMap<>();
			if (fetches != null && fetches.value() != null && fetches.value().length > 0) {
				for (Fetch fetch : fetches.value()) {
					for (String path : fetch.value()) {
						fetchingMap.put(path, fetch);
					}
				}
				filterParameter.addState(Fetches.class, fetchingMap);
			}
		}
		return super.decorateFilterParameter(filterParameter, parametersMap);
	}

}
