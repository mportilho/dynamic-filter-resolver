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

package io.github.mportilho.dfr.core.processor;

import io.github.mportilho.dfr.core.operation.FilterData;

import java.util.ArrayList;
import java.util.List;

/**
 * It's the representation of a set of logic clauses and a optional set of
 * correlated sub-statements
 *
 * @author Marcelo Portilho
 */
public record ConditionalStatement(
        String id,
        LogicType logicType,
        boolean negate,
        List<FilterData> clauses,
        List<ConditionalStatement> oppositeStatements
) {

    public List<ConditionalStatement> findStatementsById(String id) {
        List<ConditionalStatement> statementList = new ArrayList<>();
        findStatementsById(id, this, statementList);
        return statementList;
    }

    private void findStatementsById(String id, ConditionalStatement stmt, List<ConditionalStatement> statementList) {
        if (stmt.id.equals(id)) {
            statementList.add(stmt);
        }
        if (stmt.oppositeStatements != null) {
            for (ConditionalStatement subStatement : stmt.oppositeStatements) {
                findStatementsById(id, subStatement, statementList);
            }
        }
    }

    /**
     * @return An indication that this statement has no clauses nor sub-statements
     */
    public boolean hasNoCondition() {
        return clauses != null && oppositeStatements != null && clauses.isEmpty() && oppositeStatements.isEmpty();
    }

    /**
     * @return True if this statement applies conjunction logic. False if it applies
     * disjunction logic
     */
    public boolean isConjunction() {
        return LogicType.CONJUNCTION.equals(logicType);
    }

}
