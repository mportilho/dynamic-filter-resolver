package io.github.mportilho.dfr.core.processor;

import io.github.mportilho.dfr.core.operation.FilterData;

import java.util.Map;

/**
 * Defines a set of steps for creating {@link ConditionalStatement}'s
 * representation graph.
 *
 * <p>
 * Validations of any kind about the required values must not be made here, to
 * provide more flexibility to filter operation implementations
 *
 * @author Marcelo Portilho
 */
public interface ConditionalStatementProcessor<T> {

    ConditionalStatement createStatements(T parameter, Map<String, Object[]> userParameters);

    default ConditionalStatement createStatements(T userParameters) {
        return createStatements(userParameters, null);
    }

    /**
     * Creates a decorated filter data
     *
     * @param filterData     The filter parameter to be decorated
     * @param userParameters Map containing provided values for filter operations
     * @return The decorated filter parameter
     */
    FilterData decorateFilterData(FilterData filterData, Map<String, Object[]> userParameters);

}
