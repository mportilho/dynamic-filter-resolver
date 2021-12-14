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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public ConditionalStatement {
        Objects.requireNonNull(clauses, "Clause list cannot be null");
        Objects.requireNonNull(oppositeStatements, "Opposite statement list cannot be null");
    }

    public Optional<FilterData> findClauseByPath(String path) {
        return clauses.stream().filter(c -> path.equals(c.path())).findFirst();
    }

    public Optional<ConditionalStatement> findStatementsById(String id) {
        return findStatementsById(id, this);
    }

    private Optional<ConditionalStatement> findStatementsById(String id, ConditionalStatement stmt) {
        if (stmt.id.equals(id)) {
            return Optional.of(stmt);
        }
        if (stmt.oppositeStatements != null) {
            for (ConditionalStatement subStatement : stmt.oppositeStatements) {
                Optional<ConditionalStatement> optStatement = findStatementsById(id, subStatement);
                if (optStatement.isPresent()) {
                    return optStatement;
                }
            }
        }
        return Optional.empty();
    }

//    private void findStatementsById(String id, ConditionalStatement stmt, List<ConditionalStatement> statementList) {
//        if (stmt.id.equals(id)) {
//            statementList.add(stmt);
//        }
//        if (stmt.oppositeStatements != null) {
//            for (ConditionalStatement subStatement : stmt.oppositeStatements) {
//                findStatementsById(id, subStatement, statementList);
//            }
//        }
//    }

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
