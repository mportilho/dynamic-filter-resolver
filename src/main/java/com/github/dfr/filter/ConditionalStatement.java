package com.github.dfr.filter;

import java.util.Collections;
import java.util.List;

public class ConditionalStatement {

	private final LogicType logicType;
	private final boolean negate;
	private final List<FilterParameter> clauses;
	private final List<ConditionalStatement> inverseStatements;

	public ConditionalStatement(LogicType logicType, List<FilterParameter> clauses) {
		this(logicType, false, clauses, null);
	}

	public ConditionalStatement(LogicType logicType, List<FilterParameter> clauses, List<ConditionalStatement> inverseStatements) {
		this(logicType, false, clauses, inverseStatements);
	}

	public ConditionalStatement(LogicType logicType, boolean negate, List<FilterParameter> clauses, List<ConditionalStatement> inverseStatements) {
		this.logicType = logicType;
		this.negate = negate;
		this.clauses = clauses == null ? Collections.emptyList() : clauses;
		this.inverseStatements = inverseStatements == null ? Collections.emptyList() : inverseStatements;
	}

	public LogicType getLogicType() {
		return logicType;
	}

	public List<FilterParameter> getClauses() {
		return clauses;
	}

	public List<ConditionalStatement> getInverseStatements() {
		return inverseStatements;
	}

	public boolean isNegate() {
		return negate;
	}

	public boolean hasAnyCondition() {
		return !clauses.isEmpty() || !inverseStatements.isEmpty();
	}

	public boolean isConjunction() {
		return LogicType.CONJUNCTION.equals(logicType);
	}

	public boolean hasInverseStatements() {
		return inverseStatements != null && !inverseStatements.isEmpty();
	}

}
