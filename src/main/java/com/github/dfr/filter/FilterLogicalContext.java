package com.github.dfr.filter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FilterLogicalContext {

	private final LogicType logicType;
	private final CorrelatedFilterParameter correlatedFilterParameter;
	private final List<CorrelatedFilterParameter> oppositeCorrelatedFilterParameters;

	public FilterLogicalContext(LogicType logicType, CorrelatedFilterParameter correlatedFilterParameter,
			List<CorrelatedFilterParameter> oppositeCorrelatedFilterParameters) {
		this.logicType = Objects.requireNonNull(logicType, "Logic type must be informed");
		this.correlatedFilterParameter = correlatedFilterParameter;
		this.oppositeCorrelatedFilterParameters = oppositeCorrelatedFilterParameters == null ? Collections.emptyList()
				: Collections.unmodifiableList(oppositeCorrelatedFilterParameters);
	}

	public CorrelatedFilterParameter getCorrelatedFilterParameter() {
		return correlatedFilterParameter;
	}

	public List<CorrelatedFilterParameter> getOppositeCorrelatedFilterParameters() {
		return oppositeCorrelatedFilterParameters;
	}

	public boolean isConjunction() {
		return LogicType.CONJUNCTION.equals(logicType);
	}

}
