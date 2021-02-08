package io.github.mportilho.dfr.core.statement;

public enum LogicType {

	CONJUNCTION, DISJUNCTION;

	public LogicType opposite() {
		if (CONJUNCTION.equals(this)) {
			return DISJUNCTION;
		}
		return CONJUNCTION;
	}

	public boolean isConjunction() {
		return CONJUNCTION.equals(this);
	}
}