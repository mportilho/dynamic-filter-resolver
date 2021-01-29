package com.github.dfr.provider.specification.filter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.filter.CorrelatedFilterParameter;
import com.github.dfr.filter.FilterLogicContext;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.FilterParameterResolver;
import com.github.dfr.operator.FilterOperator;
import com.github.dfr.operator.FilterOperatorService;
import com.github.dfr.operator.ParameterValueConverter;

public class SpecificationFilterParameterResolver<T> implements FilterParameterResolver<Specification<T>> {

	private final FilterOperatorService<Specification<T>> filterOperatorService;
	private final ParameterValueConverter parameterValueConverter;

	public SpecificationFilterParameterResolver(FilterOperatorService<Specification<T>> operatorService,
			ParameterValueConverter parameterValueConverter) {
		this.filterOperatorService = operatorService;
		this.parameterValueConverter = parameterValueConverter;
	}

	@Override
	public Specification<T> convertTo(FilterLogicContext filterLogicContext) {
		if (filterLogicContext == null) {
			return Specification.where(null);
		}
		Map<String, Object> sharedContext = new HashMap<>();
		Specification<T> rootSpecification = Specification.where(createPredicates(filterLogicContext.getCorrelatedFilterParameter(), sharedContext));

		for (CorrelatedFilterParameter correlatedFilterParameter : filterLogicContext.getOppositeCorrelatedFilterParameters()) {
			Specification<T> specification = createPredicates(correlatedFilterParameter, sharedContext);
			if (specification != null) {
				rootSpecification = filterLogicContext.isConjunction() ? rootSpecification.and(specification) : rootSpecification.or(specification);
			}
		}
		return rootSpecification;
	}

	/**
	 * 
	 * @param correlatedParameter
	 * @return
	 */
	private Specification<T> createPredicates(CorrelatedFilterParameter correlatedParameter, Map<String, Object> sharedContext) {
		Specification<T> rootSpec = Specification.where(null);
		if (correlatedParameter == null || correlatedParameter.getFilterParameters().isEmpty()) {
			return rootSpec;
		}
		for (FilterParameter filterParameter : correlatedParameter.getFilterParameters()) {
			FilterOperator<Specification<T>> operator = filterOperatorService.getOperatorFor(filterParameter.getOperator());
			Specification<T> spec = operator.createFilter(filterParameter, parameterValueConverter, sharedContext);
			if (spec != null) {
				spec = filterParameter.isNegate() ? Specification.not(spec) : spec;
				rootSpec = correlatedParameter.isConjunction() ? rootSpec.and(spec) : rootSpec.or(spec);
			}
		}
		return rootSpec;
	}

}
