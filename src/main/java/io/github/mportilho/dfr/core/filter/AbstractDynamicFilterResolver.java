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

package io.github.mportilho.dfr.core.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.operator.FilterOperatorService;
import io.github.mportilho.dfr.core.statement.ConditionalStatement;
import io.github.mportilho.dfr.core.statement.LogicType;

/**
 * A helper class for {@link DynamicFilterResolver} that provides means to
 * facilitate iteration through the graph of {@link ConditionalStatement}
 * objects.
 * 
 * @author Marcelo Portilho
 *
 * @param <T>
 */
public abstract class AbstractDynamicFilterResolver<T> implements DynamicFilterResolver<T> {

	private final FilterOperatorService<T> filterOperatorService;
	private final FilterValueConverter filterValueConverter;

	public AbstractDynamicFilterResolver(FilterOperatorService<T> filterOperatorService, FilterValueConverter filterValueConverter) {
		this.filterOperatorService = filterOperatorService;
		this.filterValueConverter = filterValueConverter;
	}

	/**
	 * Provides the default object for when there's no conditional statement to
	 * convert
	 * 
	 * @param <R>
	 * @param <K>
	 * @param <V>
	 * @param context
	 * @return
	 */
	public abstract <R extends T, K, V> R emptyPredicate(Map<K, V> context);

	/**
	 * Converts a single statement to the desired object representation
	 * 
	 * @param <R>
	 * @param <K>
	 * @param <V>
	 * @param conditionalStatement
	 * @param context
	 * @return
	 */
	public abstract <R extends T, K, V> R createPredicateFromStatement(ConditionalStatement conditionalStatement, Map<K, V> context);

	/**
	 * Compose, when necessary, a converted statement and it's raw sub-statements to
	 * a new object representation
	 * 
	 * @param <R>
	 * @param <K>
	 * @param <V>
	 * @param logicType
	 * @param predicate
	 * @param subStatementPredicates
	 * @param context
	 * @return
	 */
	public abstract <R extends T, K, V> R composePredicatesFromSubStatements(LogicType logicType, R predicate, List<R> subStatementPredicates,
			Map<K, V> context);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R extends T, K, V> R convertTo(ConditionalStatement conditionalStatement, Map<K, V> context) {
		if (conditionalStatement == null || !conditionalStatement.hasAnyCondition()) {
			return responseDecorator(emptyPredicate(context), context);
		}
		return responseDecorator(convertRecursively(conditionalStatement, context), context);
	}

	/**
	 * Assists on navigation through the conditional statement's graph
	 * 
	 * @param <R>
	 * @param <K>
	 * @param <V>
	 * @param conditionalStatement
	 * @param context
	 * @return
	 */
	private final <R extends T, K, V> R convertRecursively(ConditionalStatement conditionalStatement, Map<K, V> context) {
		if (conditionalStatement == null || !conditionalStatement.hasAnyCondition()) {
			return null;
		}
		R predicate = createPredicateFromStatement(conditionalStatement, context);

		List<R> subStatementPredicates = new ArrayList<>();
		for (ConditionalStatement subStatement : conditionalStatement.getSubStatements()) {
			subStatementPredicates.add(convertRecursively(subStatement, context));
		}
		return composePredicatesFromSubStatements(conditionalStatement.getLogicType(), predicate, subStatementPredicates, context);
	}

	/**
	 * @param <R>
	 * @return the instance responsible to apply the filter logic operation to a
	 *         statement's clause
	 */
	@SuppressWarnings("unchecked")
	protected <R extends T> FilterOperatorService<R> getFilterOperatorService() {
		return (FilterOperatorService<R>) filterOperatorService;
	}

	/**
	 * @return the instance responsible to optionally convert variable
	 *         representation (or expression languages) to a dynamic value
	 */
	protected FilterValueConverter getFilterValueConverter() {
		return filterValueConverter;
	}

}
