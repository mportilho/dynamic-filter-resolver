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

package io.github.mportilho.dfr.providers.specification.operator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.ComparisonOperator;
import io.github.mportilho.dfr.core.operator.FilterOperator;
import io.github.mportilho.dfr.core.operator.type.Dynamic;

/**
 * The implementation of {@link Dynamic} operator for Spring Data JPA's
 * {@link Specification}
 * 
 * @author Marcelo Portilho
 *
 * @param <T>
 */
class SpecDynamic<T> implements Dynamic<Specification<T>> {

	private static final Set<String> COMPARISON_OPS;

	static {
		COMPARISON_OPS = new HashSet<>(ComparisonOperator.values().length * 2);
		for (ComparisonOperator comparisonOperator : ComparisonOperator.values()) {
			COMPARISON_OPS.add(comparisonOperator.name());
			COMPARISON_OPS.add(comparisonOperator.name().toLowerCase());
		}
	}

	private SpecificationFilterOperatorService operatorService;

	public SpecDynamic(SpecificationFilterOperatorService operatorService) {
		this.operatorService = Objects.requireNonNull(operatorService, "A filter operator service is required");
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		Object[] rawValues = filterParameter.getValues();
		if (rawValues == null || rawValues.length != 2) {
			throw new IllegalArgumentException(
					"Wrong number of values for dynamic operator. The value array must contain the filtering value and the corresponding operator");
		}

		Object value;
		ComparisonOperator comparisonOperator = convertComparisonOperator(rawValues[0]);
		if (comparisonOperator != null) {
			value = rawValues[1];
		} else {
			comparisonOperator = convertComparisonOperator(rawValues[1]);
			if (comparisonOperator != null) {
				value = rawValues[0];
			} else {
				throw new IllegalArgumentException("No valid operation was informed for dynamic comparison");
			}
		}

		FilterParameter newFilter = new FilterParameter(filterParameter.getAttributePath(), filterParameter.getPath(),
				filterParameter.getParameters(), filterParameter.getTargetType(), filterParameter.getOperator(), filterParameter.isNegate(),
				filterParameter.isIgnoreCase(), new Object[] { value }, filterParameter.getFormat());

		FilterOperator<Specification<?>> filterOperator = operatorService.getOperatorFor(comparisonOperator.getOperator());
		return (Specification<T>) filterOperator.createFilter(newFilter, filterValueConverter);
	}

	private static final ComparisonOperator convertComparisonOperator(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			String temp = (String) value;
			return temp.length() == 2 && COMPARISON_OPS.contains(temp) ? ComparisonOperator.valueOf(temp.toUpperCase()) : null;
		}
		return value instanceof ComparisonOperator ? (ComparisonOperator) value : null;
	}

}
