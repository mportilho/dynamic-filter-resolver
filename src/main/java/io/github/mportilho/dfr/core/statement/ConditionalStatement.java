/*MIT License

Copyright (c) 2021 Marcelo Portilho

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

package io.github.mportilho.dfr.core.statement;

import java.util.Collections;
import java.util.List;

import io.github.mportilho.dfr.core.filter.FilterParameter;

/**
 * It's the representation of a set of logic clauses and a optional set of
 * correlated sub-statements
 * 
 * @author Marcelo Portilho
 *
 */
public class ConditionalStatement {

	private final LogicType logicType;
	private final boolean negate;
	private final List<FilterParameter> clauses;
	private final List<ConditionalStatement> subStatements;

	public ConditionalStatement(LogicType logicType, boolean negate, List<FilterParameter> clauses) {
		this(logicType, negate, clauses, null);
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

	/**
	 * @return An indication that this statement has any clause or sub-statement
	 */
	public boolean hasAnyCondition() {
		return !clauses.isEmpty() || !subStatements.isEmpty();
	}

	/**
	 * @return True if this statement applies conjunction logic. False if it applies
	 *         disjunction logic
	 */
	public boolean isConjunction() {
		return LogicType.CONJUNCTION.equals(logicType);
	}

	@Override
	public String toString() {
		return "ConditionalStatement [logicType=" + logicType + ", negate=" + negate + ", clauses=" + (clauses == null ? null : clauses.size())
				+ ", subStatements=" + (subStatements == null ? null : subStatements.size()) + "]";
	}

}
