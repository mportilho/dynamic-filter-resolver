package com.github.dfr.filter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CorrelatedFilterParameter {

	private final LogicType logicType;
	private final List<FilterParameter> filterParameters;

	public CorrelatedFilterParameter(LogicType logicType, List<FilterParameter> parameters) {
		Objects.requireNonNull(logicType, "Logic type parameter is required");
		Objects.requireNonNull(parameters, "List of filter parameters is required");
		this.logicType = logicType;
		this.filterParameters = Collections.unmodifiableList(parameters);
	}

	public LogicType getConnectiveLogicType() {
		return logicType;
	}

	public List<FilterParameter> getFilterParameters() {
		return filterParameters;
	}

	public boolean isConjunction() {
		return LogicType.CONJUNCTION.equals(logicType);
	}

}
