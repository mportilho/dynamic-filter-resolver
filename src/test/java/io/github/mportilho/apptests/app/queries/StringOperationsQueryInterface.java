package io.github.mportilho.apptests.app.queries;

import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.EndsWith;
import io.github.mportilho.dfr.core.operator.type.EndsWithIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.EqualsIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.Like;
import io.github.mportilho.dfr.core.operator.type.LikeIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.NotEqualsIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.NotLike;
import io.github.mportilho.dfr.core.operator.type.NotLikeIgnoreCase;
import io.github.mportilho.dfr.core.operator.type.StartsWith;
import io.github.mportilho.dfr.core.operator.type.StartsWithIgnoreCase;

@Disjunction(value = { 
		@Filter(path = "name", parameters = "clientName", operator = StartsWith.class, defaultValues = "A"),
		@Filter(path = "name", parameters = "clientName", operator = StartsWithIgnoreCase.class, defaultValues = "b"),
		
		@Filter(path = "addresses.location.city", parameters = "clientName", operator = EndsWith.class, defaultValues = "c"),
		@Filter(path = "addresses.location.city", parameters = "clientName", operator = EndsWithIgnoreCase.class, defaultValues = "d"),
		
		@Filter(path = "addresses.street", parameters = "clientName", operator = EqualsIgnoreCase.class, defaultValues = "e"),
		@Filter(path = "addresses.street", parameters = "clientName", operator = NotEqualsIgnoreCase.class, defaultValues = "f"),
		
		@Filter(path = "addresses.location.state", parameters = "clientName", operator = Like.class, defaultValues = "g"),
		@Filter(path = "addresses.location.state", parameters = "clientName", operator = LikeIgnoreCase.class, defaultValues = "g"),
		
		@Filter(path = "addresses.number", parameters = "clientName", operator = NotLike.class, defaultValues = "i"),
		@Filter(path = "addresses.number", parameters = "clientName", operator = NotLikeIgnoreCase.class, defaultValues = "j")
	}
)
public interface StringOperationsQueryInterface {

}
