package net.dfr.providers.specification.filter;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.core.converter.FilterValueConverter;
import net.dfr.core.filter.AbstractDynamicFilterResolver;
import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.FilterOperator;
import net.dfr.core.operator.FilterOperatorService;
import net.dfr.core.statement.ConditionalStatement;
import net.dfr.core.statement.LogicType;
import net.dfr.providers.specification.annotation.Fetch;

public class SpecificationDynamicFilterResolver<T> extends AbstractDynamicFilterResolver<Specification<T>> {

	public SpecificationDynamicFilterResolver(FilterOperatorService<Specification<T>> filterOperatorService,
			FilterValueConverter filterValueConverter) {
		super(filterOperatorService, filterValueConverter);
	}

	@Override
	public <K, V> Specification<T> emptyPredicate(Map<K, V> context) {
		return Specification.where(null);
	}

	@Override
	public <K, V> Specification<T> createPredicate(ConditionalStatement conditionalStatement, Map<K, V> context) {
		Specification<T> rootSpec = null;
		for (FilterParameter clause : conditionalStatement.getClauses()) {
			FilterOperator<Specification<T>> operator = getFilterOperatorService().getOperatorFor(clause.getOperator());
			Specification<T> spec = operator.createFilter(clause, getFilterValueConverter());
			if (spec != null) {
				spec = clause.isNegate() ? Specification.not(spec) : spec;
				if (rootSpec == null) {
					rootSpec = spec;
				} else {
					rootSpec = conditionalStatement.isConjunction() ? rootSpec.and(spec) : rootSpec.or(spec);
				}
			}
		}
		return conditionalStatement.isNegate() ? Specification.not(rootSpec) : rootSpec;
	}

	@Override
	public <K, V> Specification<T> postCondicionalStatementResolving(LogicType logicType, Specification<T> predicate,
			List<Specification<T>> subStatementPredicates, Map<K, V> context) {
		Specification<T> currentPredicate = predicate;
		for (Specification<T> subPredicate : subStatementPredicates) {
			currentPredicate = logicType.isConjunction() ? currentPredicate.and(subPredicate) : currentPredicate.or(subPredicate);
		}
		return currentPredicate;
	}

	@Override
	public <K, V> Specification<T> responseDecorator(Specification<T> response, Map<K, V> context) {
		if (context == null || context.isEmpty()) {
			return response;
		}
		Fetch[] fetches = (Fetch[]) context.get(Fetch.class);
		if (fetches == null || fetches.length == 0) {
			return response;
		}

		Specification<T> decoratedSpec = (root, query, criteriaBuilder) -> {
			query.distinct(true);
			for (Fetch fetch : fetches) {
				for (String fetchPath : fetch.value()) {
					root.fetch(fetchPath, fetch.joinType());
				}
			}
			return null;
		};

		return decoratedSpec.and(response);
	}

}
