package com.github.dfr.filter;

import java.util.Collections;
import java.util.List;

public class ConditionalStatement {

	private final LogicType logicType;
	private final boolean negate;
	private final List<FilterParameter> clauses;
	private final List<ConditionalStatement> subStatements;

	public ConditionalStatement(LogicType logicType, List<FilterParameter> clauses) {
		this(logicType, false, clauses, null);
	}

	public ConditionalStatement(LogicType logicType, List<FilterParameter> clauses, List<ConditionalStatement> statements) {
		this(logicType, false, clauses, statements);
	}

	public ConditionalStatement(LogicType logicType, boolean negate, List<FilterParameter> clauses, List<ConditionalStatement> statements) {
		this.logicType = logicType;
		this.negate = negate;
		this.clauses = clauses == null ? Collections.emptyList() : clauses;
		this.subStatements = statements == null ? Collections.emptyList() : statements;
	}

	public LogicType getLogicType() {
		return logicType;
	}

	public List<FilterParameter> getClauses() {
		return clauses;
	}

	public List<ConditionalStatement> getSubStatements() {
		return subStatements;
	}

	public boolean isNegate() {
		return negate;
	}

	public boolean hasAnyCondition() {
		return !clauses.isEmpty() || !subStatements.isEmpty();
	}

	public boolean isConjunction() {
		return LogicType.CONJUNCTION.equals(logicType);
	}

	public boolean hasSubStatements() {
		return subStatements != null && !subStatements.isEmpty();
	}

}
