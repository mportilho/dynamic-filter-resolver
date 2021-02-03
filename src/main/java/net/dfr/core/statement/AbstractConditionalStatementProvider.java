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

import org.springframework.util.StringValueResolver;

import net.dfr.core.Pair;
import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Disjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.filter.FilterParameter;

public abstract class AbstractConditionalStatementProvider implements ConditionalStatementProvider {

	private static final List<Class<?>> IGNORED_ANNOTATIONS = Arrays.asList(Documented.class, Retention.class, Target.class);

	private StringValueResolver stringValueResolver;

	public AbstractConditionalStatementProvider(StringValueResolver stringValueResolver) {
		this.stringValueResolver = stringValueResolver;
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

		LogicType logicTypeTemp = null;
		Filter[] filters = null;
		List<Pair<Filter[], String>> inversedConditionFilters = null;
		boolean negate = false;
		if (Conjunction.class.equals(parameterAnnotation.annotationType())) {
			logicTypeTemp = LogicType.CONJUNCTION;
			Conjunction conjunction = (Conjunction) parameterAnnotation;
			filters = conjunction.value();
			negate = Boolean.parseBoolean(computeSpringExpressionLanguage(conjunction.negate()));
			inversedConditionFilters = Stream.of(conjunction.disjunctions()).filter(or -> or.value().length > 0)
					.map(or -> Pair.of(or.value(), or.negate())).collect(Collectors.toList());
		} else if (Disjunction.class.equals(parameterAnnotation.annotationType())) {
			logicTypeTemp = LogicType.DISJUNCTION;
			Disjunction disjunction = (Disjunction) parameterAnnotation;
			filters = disjunction.value();
			negate = Boolean.parseBoolean(computeSpringExpressionLanguage(disjunction.negate()));
			inversedConditionFilters = Stream.of(disjunction.conjunctions()).filter(and -> and.value().length > 0)
					.map(and -> Pair.of(and.value(), and.negate())).collect(Collectors.toList());
		}
		if (logicTypeTemp != null) {
			LogicType logicType = logicTypeTemp;
			List<FilterParameter> clauses = createFilterParameters(filters, parametersMap);
			List<ConditionalStatement> invertedStatements = (inversedConditionFilters == null) ? Collections.emptyList() : //@formatter:off
				inversedConditionFilters
					.stream()
					.filter(Objects::nonNull)
					.map(pair -> {
						List<FilterParameter> parameters = createFilterParameters(pair.getLeft(), parametersMap);
						if(parameters != null && !parameters.isEmpty()) {
							return new ConditionalStatement(logicType.opposite(), Boolean.parseBoolean(computeSpringExpressionLanguage(pair.getRight())), parameters);
						}
						return null;
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList())
			;//@formatter:on

			if (clauses.isEmpty() && !invertedStatements.isEmpty() && invertedStatements.size() == 1) {
				statements.add(invertedStatements.get(0));
			} else if (!clauses.isEmpty() || !invertedStatements.isEmpty()) {
				statements.add(new ConditionalStatement(logicType, negate, clauses, invertedStatements));
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
			if (hasAnyParameterProvidedOrConstants(parametersMap, filter)) {
				Object[] values = null;
				if (hasAnyConstantValue(filter)) {
					values = generateValuesFromConstants(filter);
				} else {
					values = generateValuesFromProvidedParameters(filter, parametersMap);
				}

				boolean negate = Boolean.parseBoolean(computeSpringExpressionLanguage(filter.negate()));
				String[] formats = computeSpringExpressionLanguage(filter.formats());

				FilterParameter parameter = new FilterParameter(filter.attributePath(), filter.path(), filter.parameters(), filter.targetType(),
						filter.operator(), negate, values, formats);
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
			for (int i = 0; i < values.length; i++) {
				if (values[i] == null && defaultValues.length > i) {
					values[i] = computeSpringExpressionLanguage(defaultValues[i]);
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
		if (stringValueResolver != null && expressions != null && expressions.length != 0) {
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
		if (stringValueResolver != null && expression != null && !expression.isEmpty()) {
			return (P) stringValueResolver.resolveStringValue(expression);
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

}
