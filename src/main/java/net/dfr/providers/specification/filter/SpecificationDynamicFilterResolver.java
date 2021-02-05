package net.dfr.providers.specification.filter;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.From;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyPath;

import net.dfr.core.converter.FilterValueConverter;
import net.dfr.core.filter.AbstractDynamicFilterResolver;
import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.FilterOperator;
import net.dfr.core.operator.FilterOperatorService;
import net.dfr.core.statement.ConditionalStatement;
import net.dfr.core.statement.LogicType;
import net.dfr.providers.specification.annotation.Fetching;

public class SpecificationDynamicFilterResolver extends AbstractDynamicFilterResolver<Specification<?>> {

	public SpecificationDynamicFilterResolver(FilterOperatorService<Specification<?>> filterOperatorService,
			FilterValueConverter filterValueConverter) {
		super(filterOperatorService, filterValueConverter);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends Specification<?>, K, V> R emptyPredicate(Map<K, V> context) {
		return (R) Specification.where(null);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <R extends Specification<?>, K, V> R createPredicate(ConditionalStatement conditionalStatement, Map<K, V> context) {
		Specification<?> rootSpec = null;
		for (FilterParameter clause : conditionalStatement.getClauses()) {
			FilterOperatorService<R> operatorService = getFilterOperatorService();
			FilterOperator<R> operator = operatorService.getOperatorFor(clause.getOperator());
			R spec = operator.createFilter(clause, getFilterValueConverter());
			if (spec != null) {
				spec = clause.isNegate() ? (R) Specification.not(spec) : spec;
				if (rootSpec == null) {
					rootSpec = (R) spec;
				} else {
					if (conditionalStatement.isConjunction()) {
						rootSpec = ((Specification) rootSpec).and(spec);
					} else {
						rootSpec = ((Specification) rootSpec).or(spec);
					}
				}
			}
		}
		return conditionalStatement.isNegate() ? (R) Specification.not(rootSpec) : (R) rootSpec;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <R extends Specification<?>, K, V> R postCondicionalStatementResolving(LogicType logicType, R predicate, List<R> subStatementPredicates,
			Map<K, V> context) {
		Specification<?> currentPredicate = predicate;
		for (Specification<?> subPredicate : subStatementPredicates) {
			if (currentPredicate == null) {
				currentPredicate = subPredicate;
			} else {
				currentPredicate = logicType.isConjunction() ? ((Specification) currentPredicate).and(subPredicate)
						: ((Specification) currentPredicate).or(subPredicate);
			}
		}
		return (R) currentPredicate;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <R extends Specification<?>, K, V> R responseDecorator(R response, Map<K, V> context) {
		if (context == null || context.isEmpty()) {
			return response;
		}
		Fetching[] fetches = (Fetching[]) context.get(Fetching.class);
		if (fetches == null || fetches.length == 0) {
			return response;
		}

		Specification<?> decoratedSpec = (root, query, criteriaBuilder) -> {
			query.distinct(true);
			for (Fetching fetching : fetches) {
				for (String attributePath : fetching.value()) {
					From<?, ?> from = root;
					PropertyPath propertyPath = PropertyPath.from(attributePath, root.getJavaType());
					if (propertyPath == null) {
						throw new IllegalArgumentException(
								String.format("No path '%s' found for type '%s'", attributePath, from.getJavaType().getSimpleName()));
					}
					while (propertyPath.hasNext()) {
						from = (From<?, ?>) root.fetch(propertyPath.getSegment(), fetching.joinType());
						propertyPath = propertyPath.next();
					}
					from.fetch(propertyPath.getSegment(), fetching.joinType());
				}
			}
			return null;
		};

		return (R) ((Specification) decoratedSpec).and(response);
	}

}
