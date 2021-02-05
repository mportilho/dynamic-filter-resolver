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
import net.dfr.core.operator.type.IsNotIn;
import net.dfr.core.operator.type.IsNotNull;
import net.dfr.core.operator.type.IsNull;
import net.dfr.core.operator.type.Less;
import net.dfr.core.operator.type.LessOrEquals;
import net.dfr.core.operator.type.Like;
import net.dfr.core.operator.type.LikeIgnoreCase;
import net.dfr.core.operator.type.NotEquals;
import net.dfr.core.operator.type.NotEqualsIgnoreCase;
import net.dfr.core.operator.type.NotLike;
import net.dfr.core.operator.type.NotLikeIgnoreCase;
import net.dfr.core.operator.type.StartsWith;
import net.dfr.core.operator.type.StartsWithIgnoreCase;

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

	@Override
	@SuppressWarnings("unchecked")
	public <R extends FilterOperator<Specification<?>>> R getOperatorFor(Class<?> operator) {
		return (R) operators.get(operator);
	}

}
