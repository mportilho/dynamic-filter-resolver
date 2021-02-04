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
	public abstract <K, V> T emptyPredicate(Map<K, V> context);

	/**
	 * 
	 * @param conditionalStatement
	 * @return
	 */
	public abstract <K, V> T createPredicate(ConditionalStatement conditionalStatement, Map<K, V> context);

	/**
	 * 
	 * @param logicType
	 * @param predicate
	 * @param statementPredicates
	 * @return
	 */
	public abstract <K, V> T postCondicionalStatementResolving(LogicType logicType, T predicate, List<T> subStatementPredicates, Map<K, V> context);

	@Override
	public <K, V> T convertTo(ConditionalStatement conditionalStatement, Map<K, V> context) {
		T result;
		if (conditionalStatement != null && conditionalStatement.hasAnyCondition()) {
			result = convertRecursively(conditionalStatement, context);
		} else {
			result = emptyPredicate(context);
		}
		return responseDecorator(result, context);
	}

	/**
	 * 
	 * @param conditionalStatement
	 * @return
	 */
	private final <K, V> T convertRecursively(ConditionalStatement conditionalStatement, Map<K, V> context) {
		if (conditionalStatement == null || !conditionalStatement.hasAnyCondition()) {
			return null;
		}
		T predicate = createPredicate(conditionalStatement, context);

		List<T> subStatementPredicates = new ArrayList<>();
		if (conditionalStatement.hasSubStatements()) {
			for (ConditionalStatement additionalStatement : conditionalStatement.getSubStatements()) {
				subStatementPredicates.add(convertRecursively(additionalStatement, context));
			}
		}
		return postCondicionalStatementResolving(conditionalStatement.getLogicType(), predicate, subStatementPredicates, context);
	}

	protected FilterOperatorService<T> getFilterOperatorService() {
		return filterOperatorService;
	}

	protected FilterValueConverter getFilterValueConverter() {
		return filterValueConverter;
	}

}
