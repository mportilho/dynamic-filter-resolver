package net.dfr.core.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.dfr.core.converter.FilterValueConverter;
import net.dfr.core.operator.FilterOperatorService;
import net.dfr.core.statement.ConditionalStatement;
import net.dfr.core.statement.LogicType;

public abstract class AbstractDynamicFilterResolver<T> implements DynamicFilterResolver<T> {

	private final FilterOperatorService<T> filterOperatorService;
	private final FilterValueConverter filterValueConverter;

	public AbstractDynamicFilterResolver(FilterOperatorService<T> filterOperatorService, FilterValueConverter filterValueConverter) {
		this.filterOperatorService = filterOperatorService;
		this.filterValueConverter = filterValueConverter;
	}

	/**
	 * 
	 * @return
	 */
	public abstract <R extends T, K, V> R emptyPredicate(Map<K, V> context);

	/**
	 * 
	 * @param conditionalStatement
	 * @return
	 */
	public abstract <R extends T, K, V> R createPredicateFromStatement(ConditionalStatement conditionalStatement, Map<K, V> context);

	/**
	 * 
	 * @param logicType
	 * @param predicate
	 * @param statementPredicates
	 * @return
	 */
	public abstract <R extends T, K, V> R composePredicatesFromSubStatements(LogicType logicType, R predicate, List<R> subStatementPredicates,
			Map<K, V> context);

	@Override
	public <R extends T, K, V> R convertTo(ConditionalStatement conditionalStatement, Map<K, V> context) {
		if (conditionalStatement == null || !conditionalStatement.hasAnyCondition()) {
			return responseDecorator(emptyPredicate(context), context);
		}
		return responseDecorator(convertRecursively(conditionalStatement, context), context);
	}

	/**
	 * 
	 * @param conditionalStatement
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

	@SuppressWarnings("unchecked")
	protected <R extends T> FilterOperatorService<R> getFilterOperatorService() {
		return (FilterOperatorService<R>) filterOperatorService;
	}

	protected FilterValueConverter getFilterValueConverter() {
		return filterValueConverter;
	}

}
