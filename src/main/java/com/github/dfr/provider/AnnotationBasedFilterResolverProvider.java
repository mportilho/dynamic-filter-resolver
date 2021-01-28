package com.github.dfr.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringValueResolver;

import com.github.dfr.annotation.Filter;
import com.github.dfr.filter.CorrelatedFilterParameter;
import com.github.dfr.filter.FilterLogicContext;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.LogicType;

public class AnnotationBasedFilterResolverProvider {

	private StringValueResolver stringValueResolver;

	public AnnotationBasedFilterResolverProvider(StringValueResolver stringValueResolver) {
		super();
		this.stringValueResolver = stringValueResolver;
	}

	/**
	 * 
	 * @param <T>
	 * @param logicType
	 * @param filters
	 * @param oppositeFiltersList
	 * @param parametersMap
	 * @return
	 */
	public <T> FilterLogicContext createLogicContext(LogicType logicType, Filter[] filters, List<Filter[]> oppositeFiltersList,
			Map<String, T[]> parametersMap) {
		List<CorrelatedFilterParameter> oppositeCorrelatedFilterList = new ArrayList<>();
		if (oppositeFiltersList != null) {
			for (Filter[] oppositeFilters : oppositeFiltersList) {
				List<FilterParameter> parameters = createFilterParameters(oppositeFilters, parametersMap);
				if (!parameters.isEmpty()) {
					oppositeCorrelatedFilterList.add(new CorrelatedFilterParameter(logicType.getOppositeLogicType(), parameters));
				}
			}
		}

		List<FilterParameter> parameterList = createFilterParameters(filters, parametersMap);
		if (oppositeCorrelatedFilterList.isEmpty() && parameterList.isEmpty()) {
			return null;
		}
		CorrelatedFilterParameter correlatedFilterParameter = new CorrelatedFilterParameter(logicType, parameterList);
		return new FilterLogicContext(logicType, correlatedFilterParameter, oppositeCorrelatedFilterList);
	}

	/**
	 * 
	 * @param <T>
	 * @param filters
	 * @param parametersMap
	 * @return
	 */
	private <T> List<FilterParameter> createFilterParameters(Filter[] filters, Map<String, T[]> parametersMap) {
		List<FilterParameter> filterParameters = new ArrayList<>();
		if (filters == null || parametersMap == null || parametersMap.isEmpty()) {
			return filterParameters;
		}

		for (Filter filter : filters) {
			if (hasAnyParameterProvided(parametersMap, filter)) {
				Object[] values = null;
				if (filter.constantValues() != null && filter.constantValues().length != 0) {
					if (filter.constantValues().length > filter.parameters().length) {
						throw new IllegalStateException(String.format("Found more constant values declared than required parameters '%s'",
								String.join(", ", Arrays.asList(filter.parameters()))));
					}
					values = computeSpringExpressionLanguage(filter.constantValues());
				} else {
					values = computeProvidedValues(filter, parametersMap);
					Object[] defaultValues = computeSpringExpressionLanguage(filter.defaultValues());
					int index = 0;
					while (values != null && values.length != 0 && index < values.length && index < defaultValues.length) {
						values[index++] = values[index] != null ? values[index] : defaultValues[index];
					}
				}
				String[] formats = (String[]) computeSpringExpressionLanguage(filter.formats());
				filterParameters.add(new FilterParameter(filter.path(), filter.parameters(), filter.targetType(), filter.decoder(), values, formats));
			}
		}
		return filterParameters;
	}

	/**
	 * 
	 * @param expressions
	 * @return
	 */
	private Object[] computeSpringExpressionLanguage(String[] expressions) {
		if (stringValueResolver != null && expressions != null && expressions.length != 0) {
			Object[] computed = new String[expressions.length];
			for (int i = 0; i < expressions.length; i++) {
				computed[i] = expressions[i] == null ? null : stringValueResolver.resolveStringValue(expressions[i]);
			}
			return computed;
		}
		return null;
	}

	/**
	 * 
	 * @param <T>
	 * @param parametersMap
	 * @param filter
	 * @return
	 */
	private <T> Object[] computeProvidedValues(Filter filter, Map<String, T[]> parametersMap) {
		Object values[] = new Object[filter.parameters().length];
		for (int i = 0; i < values.length; i++) {
			Object[] paramValues = parametersMap.get(filter.parameters()[i]);
			if (paramValues != null && paramValues.length != 0) {
				if (paramValues.length == 1) {
					values[i] = paramValues[0];
				} else {
					values[i] = paramValues;
				}
			} else {
				values[i] = null;
			}
		}
		return values;
	}

	/**
	 * 
	 * @param <T>
	 * @param parametersMap
	 * @param filter
	 * @return
	 */
	private <T> boolean hasAnyParameterProvided(Map<String, T[]> parametersMap, Filter filter) {
		for (String filterParams : filter.parameters()) {
			if (parametersMap.keySet().contains(filterParams)) {
				return true;
			}
		}
		return false;
	}

}
