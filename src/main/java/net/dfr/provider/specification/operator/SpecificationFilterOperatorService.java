package net.dfr.provider.specification.operator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.operator.FilterOperator;
import net.dfr.operator.FilterOperatorService;
import net.dfr.operator.type.Between;
import net.dfr.operator.type.EndsWith;
import net.dfr.operator.type.EndsWithIgnoreCase;
import net.dfr.operator.type.Equals;
import net.dfr.operator.type.EqualsIgnoreCase;
import net.dfr.operator.type.Greater;
import net.dfr.operator.type.GreaterOrEquals;
import net.dfr.operator.type.In;
import net.dfr.operator.type.IsNotNull;
import net.dfr.operator.type.IsNull;
import net.dfr.operator.type.Less;
import net.dfr.operator.type.LessOrEquals;
import net.dfr.operator.type.Like;
import net.dfr.operator.type.LikeIgnoreCase;
import net.dfr.operator.type.NotEquals;
import net.dfr.operator.type.NotEqualsIgnoreCase;
import net.dfr.operator.type.NotIn;
import net.dfr.operator.type.NotLike;
import net.dfr.operator.type.NotLikeIgnoreCase;
import net.dfr.operator.type.StartsWith;
import net.dfr.operator.type.StartsWithIgnoreCase;

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
