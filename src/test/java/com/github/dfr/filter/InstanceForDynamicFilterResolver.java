package com.github.dfr.filter;

import java.util.List;

public class InstanceForDynamicFilterResolver<T> extends AbstractDynamicFilterResolver<List<T>> {

	@Override
	public List<T> emptyPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> createPredicate(ConditionalStatement conditionalStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> postCondicionalStatementResolving(LogicType logicType, List<T> predicate, List<List<T>> statementPredicates) {
		// TODO Auto-generated method stub
		return null;
	}

}
