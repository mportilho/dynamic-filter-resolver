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

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.annotation.Statement;
import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.FilterOperationManager;
import io.github.mportilho.dfr.core.operation.type.IsIn;
import io.github.mportilho.dfr.core.operation.type.IsNotIn;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Marcelo Portilho
 */
public class ReflectionConditionalStatementProcessor implements ConditionalStatementProcessor<Class<?>> {

    private static final Map<ReflectionProcessorParameter, MultiValuedMap<Annotation, List<Annotation>>> cache =
            new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.WEAK, AbstractReferenceMap.ReferenceStrength.SOFT);

    private final ValueExpressionResolver valueExpressionResolver;

    public ReflectionConditionalStatementProcessor(ValueExpressionResolver valueExpressionResolver) {
        this.valueExpressionResolver = valueExpressionResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConditionalStatement createConditionalStatements(Class<?> type, Annotation[] annotations, Map<String, Object[]> parametersMap) {
        parametersMap = parametersMap != null ? parametersMap : Collections.emptyMap();
        List<ConditionalStatement> statements = new ArrayList<>();
        MultiValuedMap<Annotation, List<Annotation>> statementAnnotations = findStatementAnnotations(type, annotations);

        for (Map.Entry<Annotation, Collection<List<Annotation>>> entry : statementAnnotations.asMap().entrySet()) {
            String stmtId = entry.getKey() instanceof VirtualAnnotationHolder ?
                    ((VirtualAnnotationHolder) entry.getKey()).getClazz().getSimpleName() :
                    entry.getKey().annotationType().getSimpleName();

            List<ConditionalStatement> statementList = new ArrayList<>();
            for (List<Annotation> annotationList : entry.getValue()) {
                for (Annotation annotation : annotationList) {
                    ConditionalStatement conditionalStatementFromAnnotation = createConditionalStatementFromAnnotation(
                            stmtId, annotation, parametersMap);
                    if (conditionalStatementFromAnnotation != null) {
                        statementList.add(conditionalStatementFromAnnotation);
                    }
                }
            }
            statements.addAll(statementList);
        }

        if (statements.isEmpty()) {
            return null;
        } else if (statements.size() == 1) {
            return statements.get(0);
        }
        return new ConditionalStatement("conjunction_wrapper", LogicType.DISJUNCTION, false, null, statements);
    }

    static MultiValuedMap<Annotation, List<Annotation>> findStatementAnnotationsInternal(ReflectionProcessorParameter processorParameter) {
        MultiValuedMap<Annotation, List<Annotation>> statementAnnotations = new ArrayListValuedHashMap<>();
        for (Class<?> anInterface : getAllInterfaces(processorParameter.type())) {
            statementAnnotations.putAll(getAllAnnotations(anInterface));
        }
        statementAnnotations.putAll(getAllAnnotations(processorParameter));
        return statementAnnotations;
    }

    public static MultiValuedMap<Annotation, List<Annotation>> findStatementAnnotations(Class<?> type, Annotation[] annotations) {
        return cache.computeIfAbsent(new ReflectionProcessorParameter(type, annotations), ReflectionConditionalStatementProcessor::findStatementAnnotationsInternal);
    }

    /**
     * @param annotation    Annotation from which filter configuration will
     *                      be extracted
     * @param parametersMap Map containing provided values for filter
     *                      operations
     * @return A new {@link ConditionalStatement}
     */
    private ConditionalStatement createConditionalStatementFromAnnotation(String stmtId, Annotation annotation,
                                                                          Map<String, Object[]> parametersMap) {
        if (annotation == null) {
            return null;
        }
        ConditionalStatement statement = null;
        Conjunction conjunction = Conjunction.class.equals(annotation.annotationType()) ? (Conjunction) annotation : null;
        Disjunction disjunction = Disjunction.class.equals(annotation.annotationType()) ? (Disjunction) annotation : null;
        if (conjunction != null || disjunction != null) {
            LogicType logicType = conjunction != null ? LogicType.CONJUNCTION : LogicType.DISJUNCTION;
            boolean negate = Boolean.parseBoolean(computeSpringExpressionLanguage(conjunction != null ? conjunction.negate() : disjunction.negate(), parametersMap));
            Filter[] filters = conjunction != null ? conjunction.value() : disjunction.value();
            Statement[] statements = conjunction != null ? conjunction.disjunctions() : disjunction.conjunctions();

            List<FilterData> clauses = createFilterDatas(filters, parametersMap);
            List<ConditionalStatement> oppositeConditionals = new ArrayList<>();
            int i = 0;
            for (Statement stmt : statements) {
                List<FilterData> params = createFilterDatas(stmt.value(), parametersMap);
                if (!params.isEmpty()) {
                    oppositeConditionals.add(new ConditionalStatement(
                            stmtId + "_subStatements_" + i++,
                            logicType.opposite(),
                            Boolean.parseBoolean(computeSpringExpressionLanguage(stmt.negate(), parametersMap)),
                            params, null));
                }
            }

            if (clauses.isEmpty() && oppositeConditionals.size() == 1) {
                statement = oppositeConditionals.get(0);
            } else if (!clauses.isEmpty() || !oppositeConditionals.isEmpty()) {
                statement = new ConditionalStatement(stmtId, logicType, negate, clauses, oppositeConditionals);
            }
        }
        return statement;
    }

    /**
     * @param filters       Data for creating {@link FilterData}
     * @param parametersMap Map containing provided values for filter operations
     * @return A new list of {@link FilterData}
     */
    private List<FilterData> createFilterDatas(Filter[] filters, Map<String, Object[]> parametersMap) {
        if (filters == null || filters.length == 0) {
            return Collections.emptyList();
        }

        List<FilterData> filterParameters = new ArrayList<>();
        for (Filter filter : filters) {
            if (hasAnyParameterProvidedOrConstantsOrDefaultValues(parametersMap, filter)) {
                Object[] values;
                if (hasAnyConstantValue(filter)) {
                    values = retrieveFilterValuesFromConstants(filter, parametersMap);
                } else {
                    values = retrieveFilterValues(filter, parametersMap);
                }

                boolean negate = Boolean.parseBoolean(computeSpringExpressionLanguage(filter.negate(), parametersMap));
                String format = computeSpringExpressionLanguage(filter.format(), parametersMap);
                Map<String, String> modifiers = Arrays.stream(filter.modifiers())
                        .map(s -> {
                            String[] arr = s.split("=");
                            if (arr.length != 2) {
                                throw new IllegalArgumentException("The modifiers field format must be like 'Attribute=Value'");
                            }
                            return arr;
                        }).collect(Collectors.toMap(s -> s[0], s -> s[1]));

                FilterData parameter = new FilterData(filter.attributePath(), filter.path(), filter.parameters(), filter.targetType(),
                        filter.operation(), negate, filter.ignoreCase(), values, format, modifiers);
                parameter = decorateFilterData(parameter, parametersMap);
                filterParameters.add(parameter);
            } else if (filter.required()) {
                throw new IllegalArgumentException("No value was found for required filter " + filter.path());
            }
        }
        return filterParameters;
    }

    /**
     *
     */
    private Object[] retrieveFilterValues(Filter filter, Map<String, Object[]> parametersMap) {
        String[] parameters = filter.parameters();
        boolean multiValuedOperation = operationAcceptsMultipleValues(filter.operation());

        if (parameters == null || parameters.length == 0) {
            throw new IllegalArgumentException("No parameter configured for filter " + filter.path());
        } else if (!multiValuedOperation && filter.defaultValues() != null && filter.defaultValues().length > 0
                && filter.defaultValues().length != parameters.length) {
            throw new IllegalArgumentException("Array attribute 'defaultValues' of '@Filter' must have the same size as array attribute 'parameters' for filter " + filter.path());
        }

        if (parametersMap != null && !parametersMap.isEmpty()) {
            List<Object> valueList = new ArrayList<>();

            for (String parameterName : parameters) {
                Object[] mappedValue = parametersMap.get(parameterName);
                if (mappedValue == null) {
                    mappedValue = filter.defaultValues();
                }
                if (multiValuedOperation) {
                    if (mappedValue != null) {
                        valueList.add(mappedValue);
                    }
                } else if (mappedValue != null) {
                    if (mappedValue.length > 1) {
                        throw new IllegalArgumentException(String.format("Expecting single value for %s operation. Got multiple values", filter.operation().getSimpleName()));
                    } else if (mappedValue.length == 1) {
                        valueList.add(mappedValue[0]);
                    }
                }
            }
            return valueList.toArray(new Object[0]);
        }
        return multiValuedOperation ? new Object[]{filter.defaultValues()} : filter.defaultValues();
    }

    /**
     *
     */
    private Object[] retrieveFilterValuesFromConstants(Filter filter, Map<String, Object[]> parametersMap) {
        if (filter.constantValues().length > filter.parameters().length) {
            throw new IllegalStateException(String.format("Found more constant values declared than required parameters '%s'",
                    String.join(", ", Arrays.asList(filter.parameters()))));
        }
        return computeSpringExpressionLanguage(filter.constantValues(), parametersMap);
    }

    /**
     *
     */
    private String[] computeSpringExpressionLanguage(String[] expressions, Map<String, Object[]> parametersMap) {
        if (valueExpressionResolver != null && expressions != null && expressions.length != 0) {
            String[] computed = new String[expressions.length];
            for (int i = 0; i < expressions.length; i++) {
                computed[i] = computeSpringExpressionLanguage(expressions[i], parametersMap);
            }
            return computed;
        }
        return expressions;
    }

    /**
     *
     */
    private String computeSpringExpressionLanguage(String expression, Map<String, Object[]> parametersMap) {
        if (expression != null && !expression.isEmpty()) {
            Object[] parameterValue = parametersMap.getOrDefault(expression, null);
            if (parameterValue != null && parameterValue.length > 1) {
                throw new IllegalStateException("Expression language parsable parameter must have only one mapped value");
            } else if (ArrayUtils.isEmpty(parameterValue) && valueExpressionResolver != null) {
                return valueExpressionResolver.resolveStringValue(expression);
            }
            return parameterValue != null && parameterValue.length != 0 ? (String) parameterValue[0] : expression;
        }
        return expression;
    }

    /**
     *
     */
    private boolean hasAnyParameterProvidedOrConstantsOrDefaultValues(Map<String, Object[]> parametersMap, Filter filter) {
        if (hasAnyConstantValue(filter) || hasAnyDefaultValue(filter)) {
            return true;
        } else if (parametersMap == null) {
            return false;
        }

        Set<String> keySet = parametersMap.keySet();
        for (String param : filter.parameters()) {
            if (keySet.contains(param)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    private boolean hasAnyConstantValue(Filter filter) {
        return filter.constantValues() != null && filter.constantValues().length > 0;
    }

    /**
     *
     */
    private boolean hasAnyDefaultValue(Filter filter) {
        return filter.defaultValues() != null && filter.defaultValues().length > 0;
    }

    @SuppressWarnings("rawtypes")
    public boolean operationAcceptsMultipleValues(Class<? extends FilterOperationManager> clazz) {
        return IsIn.class == clazz || IsNotIn.class == clazz;
    }

    private static List<Class<?>> getAllInterfaces(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<Class<?>> interfacesFound = new ArrayList<>();
        interfacesFound.add(clazz);
        getAllInterfaces(clazz, interfacesFound);
        return interfacesFound;
    }

    private static void getAllInterfaces(Class<?> clazz, List<Class<?>> interfacesFound) {
        while (clazz != null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> i : interfaces) {
                if (!i.getPackageName().startsWith("java.")) {
                    interfacesFound.add(i);
                    getAllInterfaces(i, interfacesFound);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private static Map<Annotation, List<Annotation>> getAllAnnotations(ReflectionProcessorParameter processorParameter) {
        if (processorParameter.annotations() == null || processorParameter.annotations().length == 0) {
            return Collections.emptyMap();
        }
        Map<Annotation, List<Annotation>> annotationsFound = new LinkedHashMap<>();
        List<Annotation> annotations = new ArrayList<>(Arrays.asList(processorParameter.annotations()));
        annotations.removeIf(a -> a.annotationType().getPackageName().startsWith("java.lang.annotation"));
        if (!annotations.isEmpty()) {
            VirtualAnnotationHolder virtualAnnotationHolder = new VirtualAnnotationHolder(VirtualAnnotationHolder.class, annotations);
            getAllAnnotations(virtualAnnotationHolder, annotationsFound);
        }
        return annotationsFound;
    }

    private static Map<Annotation, List<Annotation>> getAllAnnotations(Class<?> type) {
        if (type == null) {
            return Collections.emptyMap();
        }
        Map<Annotation, List<Annotation>> annotationsFound = new LinkedHashMap<>();
        List<Annotation> annotations = new ArrayList<>(Arrays.asList(type.getAnnotations()));
        annotations.removeIf(a -> a.annotationType().getPackageName().startsWith("java.lang.annotation"));
        if (!annotations.isEmpty()) {
            VirtualAnnotationHolder virtualAnnotationHolder = new VirtualAnnotationHolder(type, annotations);
            getAllAnnotations(virtualAnnotationHolder, annotationsFound);
        }
        return annotationsFound;
    }

    private static boolean getAllAnnotations(Annotation annotation, Map<Annotation, List<Annotation>> annotationsFound) {
        List<Annotation> annotations;
        if (annotation instanceof VirtualAnnotationHolder virtualAnnotationHolder) {
            annotations = virtualAnnotationHolder.getAnnotations();
        } else {
            annotations = new ArrayList<>(Arrays.asList(annotation.annotationType().getAnnotations()));
        }
        annotations.removeIf(a -> a.annotationType().getPackageName().startsWith("java.lang.annotation"));

        if (annotations.isEmpty()) {
            return annotation.annotationType() == Conjunction.class || annotation.annotationType() == Disjunction.class;
        }

        for (Annotation i : annotations) {
            boolean foundStatementAnnotation = getAllAnnotations(i, annotationsFound);
            if (foundStatementAnnotation) {
                annotationsFound.computeIfAbsent(annotation, k -> new ArrayList<>()).add(i);
            }
        }
        return false;
    }

    record ReflectionProcessorParameter(Class<?> type, Annotation[] annotations) {
    }

    static class VirtualAnnotationHolder implements Annotation {

        private final Class<?> clazz;
        private final List<Annotation> annotations;

        VirtualAnnotationHolder(Class<?> clazz, List<Annotation> annotations) {
            this.clazz = clazz;
            this.annotations = annotations;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return VirtualAnnotationHolder.class;
        }

        public List<Annotation> getAnnotations() {
            return annotations;
        }

        public Class<?> getClazz() {
            return clazz;
        }
    }


}
