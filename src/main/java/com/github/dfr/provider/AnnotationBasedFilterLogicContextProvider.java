package com.github.dfr.provider;

import static com.github.dfr.filter.LogicType.CONJUNCTION;
import static com.github.dfr.filter.LogicType.DISJUNCTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringValueResolver;

import com.github.dfr.annotation.And;
import com.github.dfr.annotation.Conjunction;
import com.github.dfr.annotation.Disjunction;
import com.github.dfr.annotation.Filter;
import com.github.dfr.annotation.Or;
import com.github.dfr.filter.ConditionalStatement;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.LogicType;

public class AnnotationBasedFilterLogicContextProvider {

	private StringValueResolver stringValueResolver;

	public AnnotationBasedFilterLogicContextProvider(StringValueResolver stringValueResolver) {
		super();
		this.stringValueResolver = stringValueResolver;
	}

	/**
	 * 
	 * @param <T>
	 * @param specificationInterface
	 * @param parametersMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Specification<?>> ConditionalStatement createConditionalStatements(Class<T> specificationInterface,
			Map<String, T[]> parametersMap) {

		List<ConditionalStatement> statements = new ArrayList<>();
		for (Class<?> spec : specificationInterface.getInterfaces()) {
			if (!Specification.class.equals(spec) && Specification.class.isAssignableFrom(spec)) {
				ConditionalStatement stmt = createConditionalStatements((Class<T>) spec, parametersMap);
				statements.add(stmt);
			}
		}
		ConditionalStatement stmt = createConditionalStatementsFromInterfaceAnnotations(specificationInterface, parametersMap);
		if (stmt != null && stmt.hasAnyCondition()) {
			statements.add(stmt);
		}

		if (statements.size() == 1) {
			return statements.get(0);
		}
		return new ConditionalStatement(LogicType.CONJUNCTION, null, statements);
	}

	/**
	 * 
	 * @param <T>
	 * @param specificationInterface
	 * @param parametersMap
	 * @return
	 */
	public <T extends Specification<?>> ConditionalStatement createConditionalStatementsFromInterfaceAnnotations(Class<T> specificationInterface,
			Map<String, T[]> parametersMap) {
		return createConditionalStatements(specificationInterface.getAnnotation(And.class), specificationInterface.getAnnotation(Or.class),
				specificationInterface.getAnnotation(Conjunction.class), specificationInterface.getAnnotation(Disjunction.class), parametersMap);
	}

	/**
	 * 
	 * @param <T>
	 * @param and
	 * @param or
	 * @param conjunction
	 * @param disjunction
	 * @param parametersMap
	 * @return
	 */
	public <T> ConditionalStatement createConditionalStatements(And and, Or or, Conjunction conjunction, Disjunction disjunction,
			Map<String, T[]> parametersMap) {

		ConditionalStatement statement = null;

		if (and != null) {
			statement = createConditionalStatements(CONJUNCTION, and.value(), null, parametersMap);
		}
		if (statement == null && or != null) {
			statement = createConditionalStatements(DISJUNCTION, or.values(), null, parametersMap);
		}

		if (statement == null && conjunction != null) {
			List<Filter[]> disjunctions = new ArrayList<>(conjunction.values().length);
			for (Or subOr : conjunction.values()) {
				disjunctions.add(subOr.values());
			}
			statement = createConditionalStatements(CONJUNCTION, conjunction.and(), disjunctions, parametersMap);
		}

		if (statement == null && disjunction != null) {
			List<Filter[]> conjunctions = new ArrayList<>(disjunction.values().length);
			for (And subAnd : disjunction.values()) {
				conjunctions.add(subAnd.value());
			}
			statement = createConditionalStatements(DISJUNCTION, disjunction.or(), conjunctions, parametersMap);
		}

		return statement;
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
	public <T> ConditionalStatement createConditionalStatements(LogicType logicType, Filter[] filters, List<Filter[]> inversedConditionFilters,
			Map<String, T[]> parametersMap) {
		Map<String, T[]> filterValueMap = parametersMap == null ? Collections.emptyMap() : parametersMap;
		List<ConditionalStatement> inversedStatements = inversedConditionFilters == null ? null : //@formatter:off
			inversedConditionFilters
				.stream()
				.filter(Objects::isNull)
				.map(filter -> createFilterParameters(filters, filterValueMap))
				.filter(List::isEmpty)
				.map(params -> new ConditionalStatement(logicType.opposite(), params))
				.collect(Collectors.toList())
		;//@formatter:on

		return new ConditionalStatement(logicType, createFilterParameters(filters, filterValueMap), inversedStatements);
	}

	/**
	 * 
	 * @param <T>
	 * @param filters
	 * @param parametersMap
	 * @return
	 */
	private <T> List<FilterParameter> createFilterParameters(Filter[] filters, Map<String, T[]> parametersMap) {
		if (filters == null || filters.length == 0) {
			return Collections.emptyList();
		}

		List<FilterParameter> filterParameters = new ArrayList<>();
		for (Filter filter : filters) {
			if (hasAnyParameterProvidedOrConstants(parametersMap, filter)) {
				Object[] values = null;
				if (hasAnyConstant(filter)) {
					values = generateValuesFromFilterConstants(filter);
				} else {
					values = generateValuesFromProvidedParameters(filter, parametersMap);
				}

				boolean negate = Boolean.parseBoolean(computeSpringExpressionLanguage(filter.negate()));
				String[] formats = computeSpringExpressionLanguage(filter.formats());

				filterParameters.add(
						new FilterParameter(filter.path(), filter.parameters(), filter.targetType(), filter.operator(), negate, values, formats));
			}
		}
		return filterParameters;
	}

	/**
	 * 
	 * @param <T>
	 * @param filter
	 * @param parametersMap
	 * @return
	 */
	private <T> Object[] generateValuesFromProvidedParameters(Filter filter, Map<String, T[]> parametersMap) {
		Object[] values = computeProvidedValues(filter, parametersMap);
		String[] defaultValues = filter.defaultValues();

		if (values != null && values.length != 0 && defaultValues != null && defaultValues.length != 0) {
			for (int i = 0; i < values.length && i < defaultValues.length; i++) {
				values[i] = values[i] != null ? values[i] : computeSpringExpressionLanguage(defaultValues[i]);
			}
		}
		return values;
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	private Object[] generateValuesFromFilterConstants(Filter filter) {
		if (filter.constantValues().length > filter.parameters().length) {
			throw new IllegalStateException(String.format("Found more constant values declared than required parameters '%s'",
					String.join(", ", Arrays.asList(filter.parameters()))));
		}
		return computeSpringExpressionLanguage(filter.constantValues());
	}

	/**
	 * 
	 * @param expressions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T[] computeSpringExpressionLanguage(String[] expressions) {
		if (stringValueResolver != null && expressions != null && expressions.length != 0) {
			Object[] computed = new String[expressions.length];
			for (int i = 0; i < expressions.length; i++) {
				computed[i] = computeSpringExpressionLanguage(expressions[i]);
			}
			return (T[]) computed;
		}
		return (T[]) expressions;
	}

	/**
	 * 
	 * @param <T>
	 * @param expression
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T computeSpringExpressionLanguage(String expression) {
		if (stringValueResolver != null && expression != null && !expression.isEmpty()) {
			return (T) stringValueResolver.resolveStringValue(expression);
		}
		return (T) expression;
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
				values[i] = paramValues;
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
	private <T> boolean hasAnyParameterProvidedOrConstants(Map<String, T[]> parametersMap, Filter filter) {
		if (hasAnyConstant(filter)) {
			return true;
		}
		for (String filterParams : filter.parameters()) {
			if (parametersMap.keySet().contains(filterParams)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	private boolean hasAnyConstant(Filter filter) {
		if (filter.constantValues() != null && filter.constantValues().length > 0) {
			return true;
		}
		return false;
	}

}
