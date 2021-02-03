package net.dfr.provider;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringValueResolver;

import net.dfr.filter.AbstractConditionalStatementProvider;
import net.dfr.filter.FilterParameter;
import net.dfr.provider.specification.annotation.Fetch;
import net.dfr.provider.specification.annotation.Fetches;

public class SpecificationConditionalStatementProvider<T extends Specification<?>> extends AbstractConditionalStatementProvider<T> {

	public SpecificationConditionalStatementProvider(StringValueResolver stringValueResolver) {
		super(stringValueResolver);
	}

	@Override
	public <K, V> FilterParameter decorateFilterParameter(FilterParameter filterParameter, Map<K, V[]> parametersMap) {
		Fetches[] fetchesArray = (Fetches[]) parametersMap.get(Fetches.class);
		if (fetchesArray == null || fetchesArray.length == 0 || parametersMap.isEmpty()) {
			return filterParameter;
		}

		Fetches fetches = fetchesArray[0];
		if (fetches != null && fetches.value() != null && fetches.value().length == 0) {
			return filterParameter;
		}

		Map<String, Fetch> fetchingMap = new HashMap<>();
		for (Fetch fetch : fetches.value()) {
			for (String fetchPath : fetch.value()) {
				for (String parameterPath : filterParameter.getParameters()) {
					if (parameterPath.startsWith(fetchPath)) {
						fetchingMap.put(fetchPath, fetch);
					}
				}
			}
		}
		filterParameter.addState(Fetches.class, fetchingMap);
		return filterParameter;
	}

}
