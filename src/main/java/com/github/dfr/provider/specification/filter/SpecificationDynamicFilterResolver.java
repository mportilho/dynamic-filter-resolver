package com.github.dfr.provider.specification.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.ConditionalStatement;
import com.github.dfr.filter.DynamicFilterResolver;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.operator.FilterOperator;
import com.github.dfr.operator.FilterOperatorService;
import com.github.dfr.operator.ParameterValueConverter;

public class SpecificationDynamicFilterResolver<T> implements DynamicFilterResolver<Specification<T>> {

	private final FilterOperatorService<Specification<T>> filterOperatorService;
	private final ParameterValueConverter parameterValueConverter;

	public SpecificationDynamicFilterResolver(FilterOperatorService<Specification<T>> operatorService,
			ParameterValueConverter parameterValueConverter) {
		this.filterOperatorService = operatorService;
		this.parameterValueConverter = parameterValueConverter;
	}

	@Override
	public Specification<T> convertTo(ConditionalStatement conditionalStatement) {
		return conditionalStatement == null || !conditionalStatement.hasAnyCondition() ? Specification.where(null)
				: convertRecursively(conditionalStatement, new ConcurrentHashMap<>());
	}

	/**
	 * 
	 * @param conditionalStatement
	 * @param sharedContext
	 * @return
	 */
	public Specification<T> convertRecursively(ConditionalStatement conditionalStatement, Map<String, Object> sharedContext) {
		if (conditionalStatement == null || !conditionalStatement.hasAnyCondition()) {
			return Specification.where(null);
		}
		Specification<T> rootSpecification = Specification.where(createSpecifications(conditionalStatement, sharedContext));

		if (conditionalStatement.hasInverseStatements()) {
			for (ConditionalStatement inverseStatement : conditionalStatement.getInverseStatements()) {
				Specification<T> spec = convertRecursively(inverseStatement, sharedContext);
				rootSpecification = inverseStatement.isConjunction() ? rootSpecification.and(spec) : rootSpecification.or(spec);
			}
		}
		return rootSpecification;
	}

	/**
	 * 
	 * @param conditionalStatement
	 * @param sharedContext
	 * @return
	 */
	private Specification<T> createSpecifications(ConditionalStatement conditionalStatement, Map<String, Object> sharedContext) {
		Specification<T> rootSpec = Specification.where(null);
		for (FilterParameter clause : conditionalStatement.getClauses()) {
			FilterOperator<Specification<T>> operator = filterOperatorService.getOperatorFor(clause.getOperator());
			Specification<T> spec = operator.createFilter(clause, parameterValueConverter, sharedContext);
			if (spec != null) {
				spec = clause.isNegate() ? Specification.not(spec) : spec;
				rootSpec = conditionalStatement.isConjunction() ? rootSpec.and(spec) : rootSpec.or(spec);
			}
		}
		return conditionalStatement.isNegate() ? Specification.not(rootSpec) : rootSpec;
	}

}
