package com.github.dfr.provider.specification.filter;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.AbstractDynamicFilterResolver;
import com.github.dfr.filter.ConditionalStatement;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.LogicType;
import com.github.dfr.operator.FilterOperator;
import com.github.dfr.operator.FilterOperatorService;
import com.github.dfr.operator.FilterValueConverter;

public class SpecificationDynamicFilterResolver<T> extends AbstractDynamicFilterResolver<Specification<T>> {

	private final FilterOperatorService<Specification<T>> filterOperatorService;
	private final FilterValueConverter filterValueConverter;

	public SpecificationDynamicFilterResolver(FilterOperatorService<Specification<T>> operatorService, FilterValueConverter filterValueConverter) {
		this.filterOperatorService = operatorService;
		this.filterValueConverter = filterValueConverter;
	}

	@Override
	public Specification<T> emptyPredicate() {
		return Specification.where(null);
	}

	@Override
	public Specification<T> createPredicate(ConditionalStatement conditionalStatement) {
		Specification<T> rootSpec = Specification.where(null);
		for (FilterParameter clause : conditionalStatement.getClauses()) {
			FilterOperator<Specification<T>> operator = filterOperatorService.getOperatorFor(clause.getOperator());
			Specification<T> spec = operator.createFilter(clause, filterValueConverter, null);
			if (spec != null) {
				spec = clause.isNegate() ? Specification.not(spec) : spec;
				rootSpec = conditionalStatement.isConjunction() ? rootSpec.and(spec) : rootSpec.or(spec);
			}
		}
		return conditionalStatement.isNegate() ? Specification.not(rootSpec) : rootSpec;
	}

	@Override
	public Specification<T> postCondicionalStatementResolving(LogicType logicType, Specification<T> predicate,
			List<Specification<T>> subStatementPredicates) {
		Specification<T> currentPredicate = predicate;
		for (Specification<T> subPredicate : subStatementPredicates) {
			currentPredicate = logicType.isConjunction() ? currentPredicate.and(subPredicate) : currentPredicate.or(subPredicate);
		}
		return currentPredicate;
	}

}
