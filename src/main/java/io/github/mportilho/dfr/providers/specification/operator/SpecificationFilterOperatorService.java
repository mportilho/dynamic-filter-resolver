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

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import io.github.mportilho.dfr.core.operator.FilterOperator;
import io.github.mportilho.dfr.core.operator.FilterOperatorService;
import io.github.mportilho.dfr.core.operator.type.Between;
import io.github.mportilho.dfr.core.operator.type.EndsWith;
import io.github.mportilho.dfr.core.operator.type.EndsWithIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.operator.type.EqualsIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.Greater;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;
import io.github.mportilho.dfr.core.operator.type.IsIn;
import io.github.mportilho.dfr.core.operator.type.IsNotIn;
import io.github.mportilho.dfr.core.operator.type.IsNotNull;
import io.github.mportilho.dfr.core.operator.type.IsNull;
import io.github.mportilho.dfr.core.operator.type.Less;
import io.github.mportilho.dfr.core.operator.type.LessOrEquals;
import io.github.mportilho.dfr.core.operator.type.Like;
import io.github.mportilho.dfr.core.operator.type.LikeIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.NotEquals;
import io.github.mportilho.dfr.core.operator.type.NotEqualsIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.NotLike;
import io.github.mportilho.dfr.core.operator.type.NotLikeIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.StartsWith;
import io.github.mportilho.dfr.core.operator.type.StartsWithIgnoreCase;

/**
 * Provides instances of {@link FilterOperator} for the {@link Specification}'s
 * dynamic filter provider
 * 
 * @author Marcelo Portilho
 *
 */
public class SpecificationFilterOperatorService implements FilterOperatorService<Specification<?>> {

	@SuppressWarnings("rawtypes")
	private Map<Class<? extends FilterOperator>, FilterOperator> operators;

	@SuppressWarnings("rawtypes")
	public SpecificationFilterOperatorService() {
		operators = new HashMap<>();
		operators.put(Between.class, new SpecBetween());
		operators.put(EndsWith.class, new SpecEndsWith());
		operators.put(EndsWithIgnoreCase.class, new SpecEndsWithIgnoreCase());
		operators.put(Equals.class, new SpecEquals());
		operators.put(EqualsIgnoreCase.class, new SpecEqualsIgnoreCase());
		operators.put(Greater.class, new SpecGreater());
		operators.put(GreaterOrEquals.class, new SpecGreaterOrEquals());
		operators.put(IsIn.class, new SpecIsIn());
		operators.put(IsNotNull.class, new SpecIsNotNull());
		operators.put(IsNull.class, new SpecIsNull());
		operators.put(Less.class, new SpecLess());
		operators.put(LessOrEquals.class, new SpecLessOrEquals());
		operators.put(Like.class, new SpecLike());
		operators.put(LikeIgnoreCase.class, new SpecLikeIgnoreCase());
		operators.put(NotEquals.class, new SpecNotEquals());
		operators.put(NotEqualsIgnoreCase.class, new SpecNotEqualsIgnoreCase());
		operators.put(IsNotIn.class, new SpecIsNotIn());
		operators.put(NotLike.class, new SpecNotLike());
		operators.put(NotLikeIgnoreCase.class, new SpecNotLikeIgnoreCase());
		operators.put(StartsWith.class, new SpecStartsWith());
		operators.put(StartsWithIgnoreCase.class, new SpecStartsWithIgnoreCase());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <R extends FilterOperator<Specification<?>>> R getOperatorFor(Class<?> operator) {
		return (R) operators.get(operator);
	}

}
