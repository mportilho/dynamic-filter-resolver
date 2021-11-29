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

package io.github.mportilho.dfr.core.resolver;


import io.github.mportilho.dfr.core.processor.ConditionalStatement;
import io.github.mportilho.dfr.core.processor.LogicType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A helper class for {@link DynamicFilterResolver} that provides means to
 * facilitate iteration through the graph of {@link ConditionalStatement}
 * objects.
 *
 * @param <T> Return type of the query object created from this dynamic filter
 *            resolver
 * @author Marcelo Portilho
 */
public abstract class AbstractDynamicFilterResolver<T> implements DynamicFilterResolver<T> {

    /**
     * Provides the default object for when there's no conditional statement to
     * convert
     *
     * @param <R>     Return type of the target query object for this dynamic filter
     * @param context Context map containing helping data for statement conversion
     * @return An empty representation of the query object. Can be null
     */
    public abstract <R extends T> R emptyPredicate(Map<String, Object> context);

    /**
     * Converts a single statement to the desired object representation
     *
     * @param <R>                  Return type of the target query object for this
     *                             dynamic filter
     * @param conditionalStatement Conditional statement representation for
     *                             conversion
     * @param context              Context map containing helping data for statement
     *                             conversion
     * @return The query object created from this dynamic filter resolver
     */
    public abstract <R extends T> R createPredicateFromStatement(ConditionalStatement conditionalStatement, Map<String, Object> context);

    /**
     * Compose, when necessary, a converted statement and it's raw sub-statements to
     * a new object representation. The sub-statements have opposite logic type of its wrapper statement
     *
     * @param <R>                    Return type of the target query object for this
     *                               dynamic filter
     * @param logicType              The logic type to be used for creating the
     *                               query object
     * @param predicate              The query object created from the statement's
     *                               clauses
     * @param subStatementPredicates The sub-statements related to the current
     *                               statement being converted
     * @param context                Context map containing helping data for
     *                               statement conversion
     * @return The query object created from this dynamic filter resolver
     */
    public abstract <R extends T> R composePredicatesFromSubStatements(LogicType logicType, R predicate, List<R> subStatementPredicates,
                                                                       Map<String, Object> context);

    /**
     * {@inheritDoc}
     */
    @Override
    public <R extends T> R convertTo(ConditionalStatement conditionalStatement, Map<String, Object> context) {
        if (conditionalStatement == null || conditionalStatement.hasNoCondition()) {
            return responseDecorator(emptyPredicate(context), context);
        }
        return responseDecorator(convertRecursively(conditionalStatement, context), context);
    }

    /**
     * Assists on navigation through the conditional statement's graph
     *
     * @param <R>                  Return type of the target query object for this
     *                             dynamic filter
     * @param conditionalStatement Conditional statement representation for
     *                             conversion
     * @param context              Context map containing helping data for statement
     *                             conversion
     * @return The query object created from this dynamic filter resolver
     */
    private <R extends T> R convertRecursively(ConditionalStatement conditionalStatement, Map<String, Object> context) {
        if (conditionalStatement == null || conditionalStatement.hasNoCondition()) {
            return null;
        }
        R predicate = createPredicateFromStatement(conditionalStatement, context);

        List<R> subStatementPredicates = new ArrayList<>();
        if (conditionalStatement.oppositeStatements() != null) {
            for (ConditionalStatement subStatement : conditionalStatement.oppositeStatements()) {
                subStatementPredicates.add(convertRecursively(subStatement, context));
            }
        }
        return composePredicatesFromSubStatements(conditionalStatement.logicType(), predicate, subStatementPredicates, context);
    }

}
