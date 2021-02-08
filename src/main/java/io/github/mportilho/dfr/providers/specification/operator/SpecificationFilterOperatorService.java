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
