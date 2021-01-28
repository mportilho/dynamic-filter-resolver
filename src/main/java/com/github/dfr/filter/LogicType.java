package com.github.dfr.filter;

public enum LogicType {

	CONJUNCTION, DISJUNCTION;

	public LogicType getOppositeLogicType() {
		if (CONJUNCTION.equals(this)) {
			return DISJUNCTION;
		}
		return CONJUNCTION;
	}
}