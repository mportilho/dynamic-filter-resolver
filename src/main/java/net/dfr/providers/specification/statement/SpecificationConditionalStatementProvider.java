package net.dfr.providers.specification.statement;

import java.util.HashMap;
import java.util.Map;

import net.dfr.core.filter.FilterParameter;
import net.dfr.core.statement.AbstractConditionalStatementProvider;
import net.dfr.core.statement.ValueExpressionResolver;
import net.dfr.providers.specification.annotation.Fetch;
import net.dfr.providers.specification.annotation.Fetches;

public class SpecificationConditionalStatementProvider extends AbstractConditionalStatementProvider {

	public SpecificationConditionalStatementProvider(ValueExpressionResolver valueExpressionResolver) {
		super(valueExpressionResolver);
	}

	@Override
	public <K, V> FilterParameter decorateFilterParameter(FilterParameter filterParameter, Map<K, V[]> parametersMap) {
		if (parametersMap == null) {
			return filterParameter;
		}
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
