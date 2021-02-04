package net.dfr.providers.specification.statement;

import java.util.HashMap;
import java.util.Map;

import net.dfr.core.filter.FilterParameter;
import net.dfr.core.statement.AbstractConditionalStatementProvider;
import net.dfr.core.statement.ValueExpressionResolver;
import net.dfr.providers.specification.annotation.Fetch;

public class SpecificationConditionalStatementProvider extends AbstractConditionalStatementProvider {

	public SpecificationConditionalStatementProvider(ValueExpressionResolver valueExpressionResolver) {
		super(valueExpressionResolver);
	}

	@Override
	public <K, V> FilterParameter decorateFilterParameter(FilterParameter filterParameter, Map<K, V[]> parametersMap) {
		if (parametersMap == null || parametersMap.isEmpty()) {
			return filterParameter;
		}
		Fetch[] fetches = (Fetch[]) parametersMap.get(Fetch.class);
		if (fetches == null || fetches.length == 0) {
			return filterParameter;
		}

		Map<String, Fetch> fetchingMap = new HashMap<>();
		for (Fetch fetch : fetches) {
			for (String fetchPath : fetch.value()) {
				for (String parameterPath : filterParameter.getParameters()) {
					if (parameterPath.startsWith(fetchPath)) {
						fetchingMap.put(fetchPath, fetch);
					}
				}
			}
		}
		filterParameter.addState(Fetch.class, fetchingMap);
		return filterParameter;
	}

}
