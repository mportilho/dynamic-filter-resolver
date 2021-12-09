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


import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.processor.ConditionalStatement;
import io.github.mportilho.dfr.core.processor.LogicType;

import java.util.*;

public class GenericDynamicFilterResolver extends AbstractDynamicFilterResolver<List<?>> {

    @Override
    @SuppressWarnings("unchecked")
    public <R extends List<?>> R emptyPredicate(Map<String, Object> context) {
        return (R) Collections.emptyList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends List<?>> R createPredicateFromStatement(ConditionalStatement conditionalStatement, Map<String, Object> context) {
        if (conditionalStatement.clauses() == null) {
            return (R) Collections.emptyList();
        }
        List<Object[]> list = new ArrayList<>();
        for (FilterData filterData : conditionalStatement.clauses()) {
            List<Object[]> v = filterData.values();
            if (!v.isEmpty()) {
                list.add(v.get(0));
            }
        }
        return (R) list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends List<?>> R composePredicatesFromSubStatements(LogicType logicType, R predicate,
                                                                    List<R> subStatementPredicates, Map<String, Object> context) {
        List<R> list = new ArrayList<>();
        if (predicate != null) {
            list.addAll((Collection<? extends R>) predicate);
            if (subStatementPredicates != null) {
                for (R p : subStatementPredicates) {
                    if (p != null && !p.isEmpty()) {
                        list.addAll((Collection<? extends R>) p);
                    }
                }
            }
        }
        return (R) list;
    }

}
