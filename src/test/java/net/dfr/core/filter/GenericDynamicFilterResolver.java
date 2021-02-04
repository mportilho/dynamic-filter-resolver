package net.dfr.core.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.dfr.core.statement.ConditionalStatement;
import net.dfr.core.statement.LogicType;

public class GenericDynamicFilterResolver extends AbstractDynamicFilterResolver<List<String>> {

	public GenericDynamicFilterResolver() {
		super(null, null);
	}

	@Override
	public <K, V> List<String> emptyPredicate(Map<K, V> context) {
		return Collections.emptyList();
	}

	@Override
	public <K, V> List<String> createPredicate(ConditionalStatement conditionalStatement, Map<K, V> context) {
		return conditionalStatement.getClauses().stream().map(p -> p.getValues()).filter(v -> v != null && v.length > 0).map(v -> v[0].toString())
				.collect(Collectors.toList());
	}

	@Override
	public <K, V> List<String> postCondicionalStatementResolving(LogicType logicType, List<String> predicate, List<List<String>> statementPredicates,
			Map<K, V> context) {
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
