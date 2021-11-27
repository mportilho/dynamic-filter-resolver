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
import io.github.mportilho.dfr.core.operation.ComparisonOperator;
import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.FilterOperation;
import io.github.mportilho.dfr.core.operation.type.Dynamic;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The implementation of {@link Dynamic} operator for Spring Data JPA's
 * {@link Specification}
 *
 * @param <T>
 * @author Marcelo Portilho
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

    private final SpecificationFilterOperatorService operatorService;

    public SpecDynamic(SpecificationFilterOperatorService operatorService) {
        this.operatorService = Objects.requireNonNull(operatorService, "A filter operator service is required");
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Specification<T> createFilter(FilterData filterData, FormattedConversionService formattedConversionService) {
        Object[] rawValues = filterData.values();

        if (rawValues != null && rawValues.length == 1 && rawValues[0] instanceof Object[] && ((Object[]) rawValues[0]).length == 2) {
            rawValues = (Object[]) rawValues[0];
        } else if (rawValues == null || rawValues.length != 2) {
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

        FilterData newFilter = new FilterData(filterData.attributePath(), filterData.path(),
                filterData.parameters(), filterData.targetType(), filterData.operator(), filterData.negate(),
                filterData.ignoreCase(), new Object[]{value}, filterData.format());

        FilterOperation<Specification<?>> filterOperation = operatorService.createFilter(comparisonOperator.getOperator());
        return (Specification<T>) filterOperation.createFilter(newFilter, formattedConversionService);
    }

    private static ComparisonOperator convertComparisonOperator(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String temp) {
            return temp.length() == 2 && COMPARISON_OPS.contains(temp) ? ComparisonOperator.valueOf(temp.toUpperCase()) : null;
        }
        return value instanceof ComparisonOperator ? (ComparisonOperator) value : null;
    }

}
