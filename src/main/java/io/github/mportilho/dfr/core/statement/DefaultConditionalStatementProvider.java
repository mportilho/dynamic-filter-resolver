/*MIT License

Copyright (c) 2021 Marcelo Portilho

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

package io.github.mportilho.dfr.core.statement;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.annotation.Statement;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.FilterOperator;
import io.github.mportilho.dfr.core.operator.type.IsIn;
import io.github.mportilho.dfr.core.operator.type.IsNotIn;
import io.github.mportilho.dfr.core.operator.type.IsNotNull;
import io.github.mportilho.dfr.core.operator.type.IsNull;

/**
 * Provides a default and generic implementation of a
 * {@link ConditionalStatementProvider} that can be used on most situations
 * 
 * @author Marcelo Portilho
 *
 */
public class DefaultConditionalStatementProvider implements ConditionalStatementProvider {

	@SuppressWarnings("rawtypes")
	private static final Set<Class<? extends FilterOperator>> NULL_VALUE_OPERATORS = new HashSet<>(Arrays.asList(IsNull.class, IsNotNull.class));

	@SuppressWarnings("rawtypes")
	private static final Set<Class<? extends FilterOperator>> MULTIPLE_VALUES_OPERATORS = new HashSet<>(Arrays.asList(IsIn.class, IsNotIn.class));

	private static final Set<Class<?>> IGNORED_ANNOTATIONS = new HashSet<>(Arrays.asList(Documented.class, Retention.class, Target.class));

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

		if (parameterAnnotations != null) {
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

		for (Annotation annotation : parameterInterface.getAnnotations()) {
			ConditionalStatement annotationStatement = createConditionalStatementsFromAnnotations(annotation, parametersMap);
			if (annotationStatement != null && annotationStatement.hasAnyCondition()) {
				statements.add(annotationStatement);
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

			List<ConditionalStatement> composedConditionals = new ArrayList<>();
			for (Statement stmt : (conjunction != null ? conjunction.disjunctions() : disjunction.conjunctions())) {
				List<FilterParameter> params = createFilterParameters(stmt.value(), parametersMap);
				if (!params.isEmpty()) {
					composedConditionals.add(new ConditionalStatement(logicType.opposite(),
							Boolean.parseBoolean(computeSpringExpressionLanguage(stmt.negate())), params));
				}
			}

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
						filter.operator(), negate, filter.ignoreCase(), values, format);
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
				int size = values.length;
				for (int i = 0; i < size; i++) {
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

		String[] parameters = filter.parameters();
		Set<K> keySet = parametersMap.keySet();
		for (String param : parameters) {
			if (keySet.contains(param)) {
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
