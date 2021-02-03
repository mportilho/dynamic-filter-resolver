package net.dfr.core.filter;

import java.util.ArrayList;
import java.util.List;

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
	public abstract T emptyPredicate();

	/**
	 * 
	 * @param conditionalStatement
	 * @return
	 */
	public abstract T createPredicate(ConditionalStatement conditionalStatement);

	/**
	 * 
	 * @param logicType
	 * @param predicate
	 * @param statementPredicates
	 * @return
	 */
	public abstract T postCondicionalStatementResolving(LogicType logicType, T predicate, List<T> statementPredicates);

	@Override
	public final T convertTo(ConditionalStatement conditionalStatement) {
		T result;
		if (conditionalStatement != null && conditionalStatement.hasAnyCondition()) {
			result = convertRecursively(conditionalStatement);
		} else {
			result = emptyPredicate();
		}
		return responseDecorator(result);
	}

	/**
	 * 
	 * @param conditionalStatement
	 * @return
	 */
	private final T convertRecursively(ConditionalStatement conditionalStatement) {
		if (conditionalStatement == null || !conditionalStatement.hasAnyCondition()) {
			return null;
		}
		T predicate = createPredicate(conditionalStatement);

		List<T> subStatementPredicates = new ArrayList<>();
		if (conditionalStatement.hasSubStatements()) {
			for (ConditionalStatement additionalStatement : conditionalStatement.getSubStatements()) {
				subStatementPredicates.add(convertRecursively(additionalStatement));
			}
		}
		return postCondicionalStatementResolving(conditionalStatement.getLogicType(), predicate, subStatementPredicates);
	}

	protected FilterOperatorService<T> getFilterOperatorService() {
		return filterOperatorService;
	}

	protected FilterValueConverter getFilterValueConverter() {
		return filterValueConverter;
	}

}
