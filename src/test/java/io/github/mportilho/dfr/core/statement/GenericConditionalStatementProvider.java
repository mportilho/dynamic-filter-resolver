package io.github.mportilho.dfr.core.statement;

import io.github.mportilho.dfr.core.statement.DefaultConditionalStatementProvider;
import io.github.mportilho.dfr.core.statement.ValueExpressionResolver;

public class GenericConditionalStatementProvider extends DefaultConditionalStatementProvider {

	public GenericConditionalStatementProvider(ValueExpressionResolver valueExpressionResolver) {
		super(valueExpressionResolver);
	}

}
