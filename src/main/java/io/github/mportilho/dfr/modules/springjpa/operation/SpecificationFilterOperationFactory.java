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

import io.github.mportilho.dfr.core.operation.FilterOperationFactory;
import io.github.mportilho.dfr.core.operation.FilterOperationManager;
import io.github.mportilho.dfr.core.operation.type.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides instances of filter operations for the {@link Specification}'s
 * dynamic filter provider
 *
 * @author Marcelo Portilho
 */
public class SpecificationFilterOperationFactory implements FilterOperationFactory<Specification<?>> {

    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends FilterOperationManager>, FilterOperationManager> operationMap;

    @SuppressWarnings("rawtypes")
    public SpecificationFilterOperationFactory() {
        operationMap = new HashMap<>();
        operationMap.put(Between.class, new SpecBetween());
        operationMap.put(Dynamic.class, new SpecDynamic(this));
        operationMap.put(EndsWith.class, new SpecEndsWith());
        operationMap.put(Equals.class, new SpecEquals());
        operationMap.put(Greater.class, new SpecGreater());
        operationMap.put(GreaterOrEquals.class, new SpecGreaterOrEquals());
        operationMap.put(IsIn.class, new SpecIsIn());
        operationMap.put(IsNotNull.class, new SpecIsNotNull());
        operationMap.put(IsNull.class, new SpecIsNull());
        operationMap.put(Less.class, new SpecLess());
        operationMap.put(LessOrEquals.class, new SpecLessOrEquals());
        operationMap.put(Like.class, new SpecLike());
        operationMap.put(NotEquals.class, new SpecNotEquals());
        operationMap.put(IsNotIn.class, new SpecIsNotIn((SpecIsIn) operationMap.get(IsIn.class)));
        operationMap.put(NotLike.class, new SpecNotLike());
        operationMap.put(StartsWith.class, new SpecStartsWith());
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public FilterOperationManager<Specification<?>> createFilterManager(Class<? extends FilterOperationManager> operation) {
        return operationMap.get(operation);
    }
}
