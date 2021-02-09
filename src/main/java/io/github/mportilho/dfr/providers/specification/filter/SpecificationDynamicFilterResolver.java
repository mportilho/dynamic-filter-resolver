/*MIT License

Copyright (c) 2021 Marcelo Portilho

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

package io.github.mportilho.dfr.providers.specification.filter;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.From;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyPath;

import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.AbstractDynamicFilterResolver;
import io.github.mportilho.dfr.core.filter.DynamicFilterResolver;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.FilterOperator;
import io.github.mportilho.dfr.core.operator.FilterOperatorService;
import io.github.mportilho.dfr.core.statement.ConditionalStatement;
import io.github.mportilho.dfr.core.statement.LogicType;
import io.github.mportilho.dfr.providers.specification.annotation.Fetching;

/**
 * A {@link DynamicFilterResolver} that converts {@link ConditionalStatement}
 * into {@link Specification} instances
 * 
 * @author Marcelo Portilho
 *
 */
public class SpecificationDynamicFilterResolver extends AbstractDynamicFilterResolver<Specification<?>> {

	public SpecificationDynamicFilterResolver(FilterOperatorService<Specification<?>> filterOperatorService,
			FilterValueConverter filterValueConverter) {
		super(filterOperatorService, filterValueConverter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <R extends Specification<?>, K, V> R emptyPredicate(Map<K, V> context) {
		return (R) Specification.where(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <R extends Specification<?>, K, V> R createPredicateFromStatement(ConditionalStatement conditionalStatement, Map<K, V> context) {
		Specification rootSpec = null;
		FilterOperatorService<Specification> operatorService = getFilterOperatorService();

		for (FilterParameter clause : conditionalStatement.getClauses()) {
			FilterOperator<Specification> operator = operatorService.getOperatorFor(clause.getOperator());
			Specification spec = operator.createFilter(clause, getFilterValueConverter());
			if (spec != null) {
				spec = clause.isNegate() ? Specification.not(spec) : spec;
				if (rootSpec == null) {
					rootSpec = spec;
				} else {
					rootSpec = conditionalStatement.isConjunction() ? rootSpec.and(spec) : rootSpec.or(spec);
				}
			}
		}
		return conditionalStatement.isNegate() ? (R) Specification.not(rootSpec) : (R) rootSpec;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <R extends Specification<?>, K, V> R composePredicatesFromSubStatements(LogicType logicType, R predicate, List<R> subStatementPredicates,
			Map<K, V> context) {
		Specification currentPredicate = predicate;
		for (Specification subPredicate : subStatementPredicates) {
			if (currentPredicate == null) {
				currentPredicate = subPredicate;
			} else {
				currentPredicate = logicType.isConjunction() ? currentPredicate.and(subPredicate) : currentPredicate.or(subPredicate);
			}
		}
		return (R) currentPredicate;
	}

	/**
	 * {@inheritDoc}
	 */
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
