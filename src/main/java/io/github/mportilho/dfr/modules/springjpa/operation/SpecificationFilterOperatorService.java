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

import io.github.mportilho.dfr.core.operation.FilterOperation;
import io.github.mportilho.dfr.core.operation.FilterOperatorFactory;
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
public class SpecificationFilterOperatorService implements FilterOperatorFactory<Specification<?>> {

    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends FilterOperation>, FilterOperation> operators;

    @SuppressWarnings("rawtypes")
    public SpecificationFilterOperatorService() {
        operators = new HashMap<>();
        operators.put(Between.class, new SpecBetween());
        operators.put(Dynamic.class, new SpecDynamic(this));
        operators.put(EndsWith.class, new SpecEndsWith());
        operators.put(Equals.class, new SpecEquals());
        operators.put(Greater.class, new SpecGreater());
        operators.put(GreaterOrEquals.class, new SpecGreaterOrEquals());
        operators.put(IsIn.class, new SpecIsIn());
        operators.put(IsNotNull.class, new SpecIsNotNull());
        operators.put(IsNull.class, new SpecIsNull());
        operators.put(Less.class, new SpecLess());
        operators.put(LessOrEquals.class, new SpecLessOrEquals());
        operators.put(Like.class, new SpecLike());
        operators.put(NotEquals.class, new SpecNotEquals());
        operators.put(IsNotIn.class, new SpecIsNotIn((SpecIsIn) operators.get(IsIn.class)));
        operators.put(NotLike.class, new SpecNotLike());
        operators.put(StartsWith.class, new SpecStartsWith());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends FilterOperation<Specification<?>>> R createFilter(Class<? extends FilterOperation<Specification<?>>> operator) {
        return (R) operators.get(operator);
    }
}
