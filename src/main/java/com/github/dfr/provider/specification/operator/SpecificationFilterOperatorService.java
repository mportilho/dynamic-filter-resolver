package com.github.dfr.provider.specification.operator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.github.dfr.operator.FilterOperator;
import com.github.dfr.operator.FilterOperatorService;
import com.github.dfr.operator.type.Between;
import com.github.dfr.operator.type.EndsWith;
import com.github.dfr.operator.type.EndsWithIgnoreCase;
import com.github.dfr.operator.type.Equals;
import com.github.dfr.operator.type.EqualsIgnoreCase;
import com.github.dfr.operator.type.Greater;
import com.github.dfr.operator.type.GreaterOrEquals;
import com.github.dfr.operator.type.In;
import com.github.dfr.operator.type.IsNotNull;
import com.github.dfr.operator.type.IsNull;
import com.github.dfr.operator.type.Less;
import com.github.dfr.operator.type.LessOrEquals;
import com.github.dfr.operator.type.Like;
import com.github.dfr.operator.type.LikeIgnoreCase;
import com.github.dfr.operator.type.NotEquals;
import com.github.dfr.operator.type.NotEqualsIgnoreCase;
import com.github.dfr.operator.type.NotIn;
import com.github.dfr.operator.type.NotLike;
import com.github.dfr.operator.type.NotLikeIgnoreCase;
import com.github.dfr.operator.type.StartsWith;
import com.github.dfr.operator.type.StartsWithIgnoreCase;

public class SpecificationFilterOperatorService<T> implements FilterOperatorService<Specification<T>> {

	@SuppressWarnings("rawtypes")
	private Map<Class<? extends FilterOperator>, FilterOperator<Specification<T>>> operators;

	public SpecificationFilterOperatorService() {
		operators = new HashMap<>();
		operators.put(Between.class, new SpecBetween<T>());
		operators.put(EndsWith.class, new SpecEndsWith<T>());
		operators.put(EndsWithIgnoreCase.class, new SpecEndsWithIgnoreCase<T>());
		operators.put(Equals.class, new SpecEquals<T>());
		operators.put(EqualsIgnoreCase.class, new SpecEqualsIgnoreCase<T>());
		operators.put(Greater.class, new SpecGreater<T>());
		operators.put(GreaterOrEquals.class, new SpecGreaterOrEquals<T>());
		operators.put(In.class, new SpecIn<T>());
		operators.put(IsNotNull.class, new SpecIsNotNull<T>());
		operators.put(IsNull.class, new SpecIsNull<T>());
		operators.put(Less.class, new SpecLess<T>());
		operators.put(LessOrEquals.class, new SpecLessOrEquals<T>());
		operators.put(Like.class, new SpecLike<T>());
		operators.put(LikeIgnoreCase.class, new SpecLikeIgnoreCase<T>());
		operators.put(NotEquals.class, new SpecNotEquals<T>());
		operators.put(NotEqualsIgnoreCase.class, new SpecNotEqualsIgnoreCase<T>());
		operators.put(NotIn.class, new SpecNotIn<T>());
		operators.put(NotLike.class, new SpecNotLike<T>());
		operators.put(NotLikeIgnoreCase.class, new SpecNotLikeIgnoreCase<T>());
		operators.put(StartsWith.class, new SpecStartsWith<T>());
		operators.put(StartsWithIgnoreCase.class, new SpecStartsWithIgnoreCase<T>());
	}

	@Override
	public FilterOperator<Specification<T>> getOperatorFor(Class<? extends FilterOperator<?>> operator) {
		return operators.get(operator);
	}

}
