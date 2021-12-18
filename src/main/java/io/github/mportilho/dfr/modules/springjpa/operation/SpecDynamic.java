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
import io.github.mportilho.dfr.core.operation.ComparisonOperation;
import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.type.Dynamic;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The implementation of {@link Dynamic} operation for Spring Data JPA's
 * {@link Specification}
 *
 * @param <T>
 * @author Marcelo Portilho
 */
class SpecDynamic<T> implements Dynamic<Specification<T>> {

    private final SpecificationFilterOperationService filterOperationService;
    private final Set<String> comparisonOperationNames;

    public SpecDynamic(SpecificationFilterOperationService filterOperationService) {
        this.filterOperationService = Objects.requireNonNull(filterOperationService, "Filter Operation Service required");
        this.comparisonOperationNames = Arrays.stream(ComparisonOperation.values()).map(Enum::name)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Specification<T> createFilter(FilterData filterData, FormattedConversionService formattedConversionService) {
        Object[] rawValues = filterData.values().get(0);
        if (rawValues == null || rawValues.length != 2) {
            throw new IllegalArgumentException("Must provide only operation and value for dynamic query");
        }

        Object value;
        ComparisonOperation comparisonOperation = convertComparisonOperation(rawValues[0]);
        if (comparisonOperation != null) {
            value = rawValues[1];
        } else {
            comparisonOperation = convertComparisonOperation(rawValues[1]);
            if (comparisonOperation != null) {
                value = rawValues[0];
            } else {
                throw new IllegalArgumentException("Must provide only operation and value for dynamic query");
            }
        }

        FilterData newFilter = new FilterData(filterData.attributePath(), filterData.path(),
                filterData.parameters(), filterData.targetType(), comparisonOperation.getOperation(), filterData.negate(),
                filterData.ignoreCase(), List.<Object[]>of(new Object[]{value}), filterData.format(), filterData.modifiers());

        return filterOperationService.createFilter(newFilter);
    }

    private ComparisonOperation convertComparisonOperation(Object value) {
        if (value instanceof String temp && temp.length() == 2) {
            String upper = temp.toUpperCase();
            return comparisonOperationNames.contains(upper) ? ComparisonOperation.valueOf(upper) : null;
        }
        return value instanceof ComparisonOperation operation ? operation : null;
    }

}
