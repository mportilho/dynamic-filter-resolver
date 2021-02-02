package com.github.dfr.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GenericDynamicFilterResolver extends AbstractDynamicFilterResolver<List<String>> {

	@Override
	public List<String> emptyPredicate() {
		return Collections.emptyList();
	}

	@Override
	public List<String> createPredicate(ConditionalStatement conditionalStatement) {
		return conditionalStatement.getClauses().stream().map(p -> p.getValues()).filter(v -> v != null && v.length > 0).map(v -> v[0].toString())
				.collect(Collectors.toList());
	}

	@Override
	public List<String> postCondicionalStatementResolving(LogicType logicType, List<String> predicate, List<List<String>> statementPredicates) {
		List<String> list = new ArrayList<>();
		if (predicate != null) {
			list.addAll(predicate);
			if (statementPredicates != null) {
				statementPredicates.stream().filter(p -> p != null && !p.isEmpty()).forEach(list::addAll);
			}
		}
		return list;
	}

}
