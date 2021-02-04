package net.dfr.providers.specification.operator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.core.operator.FilterOperator;
import net.dfr.core.operator.FilterOperatorService;
import net.dfr.core.operator.type.Between;
import net.dfr.core.operator.type.EndsWith;
import net.dfr.core.operator.type.EndsWithIgnoreCase;
import net.dfr.core.operator.type.Equals;
import net.dfr.core.operator.type.EqualsIgnoreCase;
import net.dfr.core.operator.type.Greater;
import net.dfr.core.operator.type.GreaterOrEquals;
import net.dfr.core.operator.type.IsIn;
import net.dfr.core.operator.type.IsNotNull;
import net.dfr.core.operator.type.IsNull;
import net.dfr.core.operator.type.Less;
import net.dfr.core.operator.type.LessOrEquals;
import net.dfr.core.operator.type.Like;
import net.dfr.core.operator.type.LikeIgnoreCase;
import net.dfr.core.operator.type.NotEquals;
import net.dfr.core.operator.type.NotEqualsIgnoreCase;
import net.dfr.core.operator.type.IsNotIn;
import net.dfr.core.operator.type.NotLike;
import net.dfr.core.operator.type.NotLikeIgnoreCase;
import net.dfr.core.operator.type.StartsWith;
import net.dfr.core.operator.type.StartsWithIgnoreCase;

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
		operators.put(IsIn.class, new SpecIsIn<T>());
		operators.put(IsNotNull.class, new SpecIsNotNull<T>());
		operators.put(IsNull.class, new SpecIsNull<T>());
		operators.put(Less.class, new SpecLess<T>());
		operators.put(LessOrEquals.class, new SpecLessOrEquals<T>());
		operators.put(Like.class, new SpecLike<T>());
		operators.put(LikeIgnoreCase.class, new SpecLikeIgnoreCase<T>());
		operators.put(NotEquals.class, new SpecNotEquals<T>());
		operators.put(NotEqualsIgnoreCase.class, new SpecNotEqualsIgnoreCase<T>());
		operators.put(IsNotIn.class, new SpecIsNotIn<T>());
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
