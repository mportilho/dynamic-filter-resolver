package net.dfr.core.statement;

import org.springframework.util.StringValueResolver;

import net.dfr.core.statement.AbstractConditionalStatementProvider;

public class GenericConditionalStatementProvider extends AbstractConditionalStatementProvider {

	public GenericConditionalStatementProvider(StringValueResolver stringValueResolver) {
		super(stringValueResolver);
	}

}
