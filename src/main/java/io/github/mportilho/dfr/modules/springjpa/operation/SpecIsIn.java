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

package io.github.mportilho.dfr.modules.springjpa.operation;

import io.github.mportilho.dfr.converters.FormattedConversionService;
import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.type.IsIn;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 * Implementation of {@link IsIn} for the Spring Data JPA's
 * {@link Specification} interface
 *
 * @param <T>
 * @author Marcelo Portilho
 */
class SpecIsIn<T> implements IsIn<Specification<T>> {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Specification<T> createFilter(FilterData filterData, FormattedConversionService formattedConversionService) {
        return (root, query, criteriaBuilder) -> {
            Expression expression = PredicateUtils.computeAttributePath(filterData, root);
            Object[] rawValues = extractArrayFromParameter(filterData.values());
            Predicate predicate = null;
            boolean ignoreCase = filterData.ignoreCase() && expression.getJavaType().equals(String.class);

            if (rawValues != null) {
                if (ignoreCase) {
                    expression = criteriaBuilder.upper(expression);
                }

                int size = rawValues.length;
                Object[] value = new Object[size];
                for (int i = 0; i < size; i++) {
                    Object currVal = formattedConversionService.convert(rawValues[i], expression.getJavaType(), filterData.format());
                    if (ignoreCase) {
                        currVal = currVal != null ? currVal.toString().toUpperCase() : null;
                    }
                    value[i] = currVal;
                }
                predicate = expression.in(value);
            }
            return predicate;
        };
    }

    /**
     * Normalize array parameter for 'IN' operation
     */
    private Object[] extractArrayFromParameter(Object[] paramValues) {
        if (paramValues == null || paramValues.length == 0) {
            return null;
        } else if (paramValues.getClass().isArray()) {
            if (paramValues.length == 1 && paramValues[0].getClass().isArray()) {
                return (Object[]) paramValues[0];
            }
            return paramValues;
        } else {
            throw new IllegalArgumentException("Expecting parameter value to be an array");
        }
    }

}
