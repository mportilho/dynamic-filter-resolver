package net.dfr.core.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.dfr.core.statement.ConditionalStatement;
import net.dfr.core.statement.LogicType;

public class GenericDynamicFilterResolver extends AbstractDynamicFilterResolver<List<?>> {

	public GenericDynamicFilterResolver() {
		super(null, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends List<?>, K, V> R emptyPredicate(Map<K, V> context) {
		return (R) Collections.emptyList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends List<?>, K, V> R createPredicateFromStatement(ConditionalStatement conditionalStatement, Map<K, V> context) {
		return (R) conditionalStatement.getClauses().stream().map(p -> p.getValues()).filter(v -> v != null && v.length > 0).map(v -> v[0].toString())
				.collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends List<?>, K, V> R composePredicatesFromSubStatements(LogicType logicType, R predicate, List<R> subStatementPredicates,
			Map<K, V> context) {
		List<R> list = new ArrayList<>();
		if (predicate != null) {
			list.addAll((Collection<? extends R>) predicate);
			if (subStatementPredicates != null) {
				subStatementPredicates.stream().filter(p -> p != null && !p.isEmpty()).forEach(p -> {
					list.addAll((Collection<? extends R>) p);
				});
			}
		}
		return (R) list;
	}

}
