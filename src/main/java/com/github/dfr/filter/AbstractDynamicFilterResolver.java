package com.github.dfr.filter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDynamicFilterResolver<T> implements DynamicFilterResolver<T> {

	public abstract T emptyPredicate();

	public abstract T createPredicate(ConditionalStatement conditionalStatement);

	public abstract T postCondicionalStatementResolving(LogicType logicType, T predicate, List<T> statementPredicates);

	@Override
	public T convertTo(ConditionalStatement conditionalStatement) {
		T result = conditionalStatement == null || !conditionalStatement.hasAnyCondition() ? emptyPredicate()
				: convertRecursively(conditionalStatement);
		return result;
	}

	private final T convertRecursively(ConditionalStatement conditionalStatement) {
		if (conditionalStatement == null || !conditionalStatement.hasAnyCondition()) {
			return emptyPredicate();
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

}
