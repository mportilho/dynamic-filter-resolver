package net.dfr.core.statement;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Disjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.FilterOperator;
import net.dfr.core.operator.type.IsIn;
import net.dfr.core.operator.type.IsNotIn;
import net.dfr.core.operator.type.IsNotNull;
import net.dfr.core.operator.type.IsNull;

public class DefaultConditionalStatementProvider implements ConditionalStatementProvider {

	@SuppressWarnings("rawtypes")
	private static final List<Class<? extends FilterOperator>> NULL_VALUE_OPERATORS = Arrays.asList(IsNull.class, IsNotNull.class);

	@SuppressWarnings("rawtypes")
	private static final List<Class<? extends FilterOperator>> MULTIPLE_VALUES_OPERATORS = Arrays.asList(IsIn.class, IsNotIn.class);

	private static final List<Class<?>> IGNORED_ANNOTATIONS = Arrays.asList(Documented.class, Retention.class, Target.class);

	private ValueExpressionResolver valueExpressionResolver;

	public DefaultConditionalStatementProvider(ValueExpressionResolver valueExpressionResolver) {
		this.valueExpressionResolver = valueExpressionResolver;
	}

	/**
	 * 
	 * @param <P>
	 * @param parameterInterface
	 * @param parameterAnnotations
	 * @param parametersMap
	 * @return
	 */
	@Override
	public <K, V> ConditionalStatement createConditionalStatements(Class<?> parameterInterface, Annotation[] parameterAnnotations,
			Map<K, V[]> parametersMap) {
		List<ConditionalStatement> statements = new ArrayList<>();

		ConditionalStatement interfaceStatement = createConditionalStatementsFromParameterInterface(parameterInterface, parametersMap);
		if (interfaceStatement != null && interfaceStatement.hasAnyCondition()) {
			statements.add(interfaceStatement);
		}

		if (parameterAnnotations != null && parameterAnnotations.length != 0) {
			for (Annotation annotation : parameterAnnotations) {
				ConditionalStatement annotationStatement = createConditionalStatementsFromAnnotations(annotation, parametersMap);
				if (annotationStatement != null && annotationStatement.hasAnyCondition()) {
					statements.add(annotationStatement);
				}
			}
		}

		if (statements.isEmpty()) {
			return null;
		} else if (statements.size() == 1) {
			return statements.get(0);
		}
		return new ConditionalStatement(LogicType.CONJUNCTION, false, null, statements);
	}

	/**
	 * 
	 * @param <P>
	 * @param parameterInterface
	 * @param parametersMap
	 * @return
	 */
	private <K, V> ConditionalStatement createConditionalStatementsFromParameterInterface(Class<?> parameterInterface, Map<K, V[]> parametersMap) {
		List<ConditionalStatement> statements = new ArrayList<>();
		for (Class<?> subInterfaces : parameterInterface.getInterfaces()) {
			ConditionalStatement stmt = createConditionalStatementsFromParameterInterface(subInterfaces, parametersMap);
			if (stmt != null && stmt.hasAnyCondition()) {
				statements.add(stmt);
			}
		}

		Annotation[] parameterAnnotations = parameterInterface.getAnnotations();
		if (parameterAnnotations != null && parameterAnnotations.length != 0) {
			for (Annotation annotation : parameterAnnotations) {
				ConditionalStatement annotationStatement = createConditionalStatementsFromAnnotations(annotation, parametersMap);
				if (annotationStatement != null && annotationStatement.hasAnyCondition()) {
					statements.add(annotationStatement);
				}
			}
		}

		if (statements.isEmpty()) {
			return null;
		} else if (statements.size() == 1) {
			return statements.get(0);
		}
		return new ConditionalStatement(LogicType.CONJUNCTION, false, null, statements);
	}

	/**
	 * 
	 * @param <P>
	 * @param parameterAnnotations
	 * @param parametersMap
	 * @return
	 */
	private <K, V> ConditionalStatement createConditionalStatementsFromAnnotations(Annotation parameterAnnotation, Map<K, V[]> parametersMap) {
		if (parameterAnnotation == null) {
			return null;
		}
		List<ConditionalStatement> statements = new ArrayList<>();
		if (!parameterAnnotation.annotationType().equals(Conjunction.class) && !parameterAnnotation.annotationType().equals(Disjunction.class)) {
			for (Annotation annotation : parameterAnnotation.annotationType().getAnnotations()) {
				if (!IGNORED_ANNOTATIONS.contains(annotation.annotationType())) {
					ConditionalStatement stmt = createConditionalStatementsFromAnnotations(annotation, parametersMap);
					if (stmt != null && stmt.hasAnyCondition()) {
						statements.add(stmt);
					}
				}
			}
		}
		Conjunction conjunction = Conjunction.class.equals(parameterAnnotation.annotationType()) ? (Conjunction) parameterAnnotation : null;
		Disjunction disjunction = Disjunction.class.equals(parameterAnnotation.annotationType()) ? (Disjunction) parameterAnnotation : null;
		if (conjunction != null || disjunction != null) {
			LogicType logicType = conjunction != null ? LogicType.CONJUNCTION : LogicType.DISJUNCTION;
			Boolean negate = Boolean.parseBoolean(computeSpringExpressionLanguage(conjunction != null ? conjunction.negate() : disjunction.negate()));

			List<FilterParameter> clauses = createFilterParameters(conjunction != null ? conjunction.value() : disjunction.value(), parametersMap);
			List<ConditionalStatement> composedConditionals = Stream //
					.of(conjunction != null ? conjunction.disjunctions() : disjunction.conjunctions()) //
					.filter(Objects::nonNull) //
					.flatMap(stmts -> Stream.of(stmts)) //
					.map(stmt -> {
						List<FilterParameter> params = createFilterParameters(stmt.value(), parametersMap);
						if (params != null && !params.isEmpty()) {
							return new ConditionalStatement(logicType.opposite(),
									Boolean.parseBoolean(computeSpringExpressionLanguage(stmt.negate())), params);
						}
						return null;
					}) //
					.filter(Objects::nonNull) //
					.collect(Collectors.toList());
			if (clauses.isEmpty() && !composedConditionals.isEmpty() && composedConditionals.size() == 1) {
				statements.add(composedConditionals.get(0));
			} else if (!clauses.isEmpty() || !composedConditionals.isEmpty()) {
				statements.add(new ConditionalStatement(logicType, negate, clauses, composedConditionals));
			}
		}

		if (statements.isEmpty()) {
			return null;
		} else if (statements.size() == 1) {
			return statements.get(0);
		}
		return new ConditionalStatement(LogicType.CONJUNCTION, false, null, statements);
	}

	/**
	 * 
	 * @param filters
	 * @param parametersMap
	 * @return
	 */
	private final <K, V> List<FilterParameter> createFilterParameters(Filter[] filters, Map<K, V[]> parametersMap) {
		if (filters == null || filters.length == 0) {
			return Collections.emptyList();
		}

		List<FilterParameter> filterParameters = new ArrayList<>();
		for (Filter filter : filters) {
			if (operatorAcceptsNullValues(filter.operator()) || hasAnyParameterProvidedOrConstants(parametersMap, filter)) {
				Object[] values = null;
				if (hasAnyConstantValue(filter)) {
					values = generateValuesFromConstants(filter);
				} else {
					values = generateValuesFromProvidedParameters(filter, parametersMap);
				}

				boolean negate = Boolean.parseBoolean(computeSpringExpressionLanguage(filter.negate()));
				String format = computeSpringExpressionLanguage(filter.format());

				FilterParameter parameter = new FilterParameter(filter.attributePath(), filter.path(), filter.parameters(), filter.targetType(),
						filter.operator(), negate, values, format);
				parameter = decorateFilterParameter(parameter, parametersMap);
				filterParameters.add(parameter);
			}
		}
		return filterParameters;
	}

	/**
	 * 
	 * @param filter
	 * @param parametersMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <K, V> V[] generateValuesFromProvidedParameters(Filter filter, Map<K, V[]> parametersMap) {
		Object[] values = computeProvidedValues(filter, parametersMap);
		if (values.length > 0) {
			String[] defaultValues = filter.defaultValues();
			if (operatorAcceptsMultipleValues(filter.operator())) {
				values[0] = defaultValues;
			} else {
				for (int i = 0; i < values.length; i++) {
					if (values[i] == null && defaultValues.length > i) {
						values[i] = computeSpringExpressionLanguage(defaultValues[i]);
					}
				}
			}
		}
		return (V[]) values;
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	private Object[] generateValuesFromConstants(Filter filter) {
		if (filter.constantValues().length > filter.parameters().length) {
			throw new IllegalStateException(String.format("Found more constant values declared than required parameters '%s'",
					String.join(", ", Arrays.asList(filter.parameters()))));
		}
		return computeSpringExpressionLanguage(filter.constantValues());
	}

	/**
	 * 
	 * @param parametersMap
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <K, V> V[] computeProvidedValues(Filter filter, Map<K, V[]> parametersMap) {
		V[] values = (V[]) new Object[filter.parameters().length];
		if (parametersMap != null && !parametersMap.isEmpty()) {
			for (int i = 0; i < values.length; i++) {
				V[] paramValues = parametersMap.get(filter.parameters()[i]);
				if (paramValues != null && paramValues.length != 0) {
					values[i] = paramValues.length == 1 ? paramValues[0] : (V) paramValues;
				} else {
					values[i] = (V) paramValues;
				}
			}
		}
		return values;
	}

	/**
	 * 
	 * @param <P>
	 * @param expressions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <P> P[] computeSpringExpressionLanguage(String[] expressions) {
		if (valueExpressionResolver != null && expressions != null && expressions.length != 0) {
			Object[] computed = new String[expressions.length];
			for (int i = 0; i < expressions.length; i++) {
				computed[i] = computeSpringExpressionLanguage(expressions[i]);
			}
			return (P[]) computed;
		}
		return (P[]) expressions;
	}

	/**
	 * 
	 * @param <P>
	 * @param expression
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private final <P> P computeSpringExpressionLanguage(String expression) {
		if (valueExpressionResolver != null && expression != null && !expression.isEmpty()) {
			return (P) valueExpressionResolver.resolveStringValue(expression);
		}
		return (P) expression;
	}

	/**
	 * 
	 * @param parametersMap
	 * @param filter
	 * @return
	 */
	private <K, V> boolean hasAnyParameterProvidedOrConstants(Map<K, V[]> parametersMap, Filter filter) {
		if (hasAnyConstantValue(filter) || hasAnyDefaultValue(filter)) {
			return true;
		} else if (parametersMap == null) {
			return false;
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
	private boolean hasAnyConstantValue(Filter filter) {
		if (filter.constantValues() != null && filter.constantValues().length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	private boolean hasAnyDefaultValue(Filter filter) {
		if (filter.defaultValues() != null && filter.defaultValues().length > 0) {
			return true;
		}
		return false;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean operatorAcceptsNullValues(Class<? extends FilterOperator> clazz) {
		return NULL_VALUE_OPERATORS.contains(clazz);
	}

	@SuppressWarnings("rawtypes")
	public boolean operatorAcceptsMultipleValues(Class<? extends FilterOperator> clazz) {
		return MULTIPLE_VALUES_OPERATORS.contains(clazz);
	}

}
