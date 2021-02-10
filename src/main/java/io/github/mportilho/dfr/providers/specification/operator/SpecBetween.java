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

import javax.persistence.criteria.Expression;

import org.springframework.data.jpa.domain.Specification;

import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.type.Between;

/**
 * Implementation of {@link Between} for the Spring Data JPA's
 * {@link Specification} interface
 * 
 * @author Marcelo Portilho
 *
 * @param <T>
 */
class SpecBetween<T> implements Between<Specification<T>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Expression<Comparable> expression = PredicateUtils.computeAttributePath(filterParameter, root);
			Comparable lowerValue;
			Comparable upperValue;

			if (filterParameter.getValues() == null) {
				lowerValue = null;
				upperValue = null;
			} else if (filterParameter.getValues().length == 1 || filterParameter.getValues().length > 2) {
				throw new IllegalStateException(
						"Wrong number of arguments for between operation. Needs 2 arguments, have " + filterParameter.getValues().length);
			} else {
				lowerValue = (Comparable) filterParameter.getValues()[0];
				lowerValue = filterValueConverter.convert(lowerValue, expression.getJavaType(), filterParameter.getFormat());

				upperValue = (Comparable) filterParameter.getValues()[1];
				upperValue = filterValueConverter.convert(upperValue, expression.getJavaType(), filterParameter.getFormat());

				if (filterParameter.isIgnoreCase() && expression.getJavaType().equals(String.class)) {
					expression = criteriaBuilder.upper((Expression) expression);
					lowerValue = transformNonNull(lowerValue, v -> (Comparable<String>) v.toString().toUpperCase());
					upperValue = transformNonNull(upperValue, v -> (Comparable<String>) v.toString().toUpperCase());
				}
			}

			return criteriaBuilder.between(expression, lowerValue, upperValue);
		};
	}

}
