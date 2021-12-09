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

package io.github.mportilho.dfr.core.processor.impl;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.annotation.Statement;
import io.github.mportilho.dfr.core.operation.FilterData;
import io.github.mportilho.dfr.core.operation.type.IsIn;
import io.github.mportilho.dfr.core.operation.type.IsNotIn;
import io.github.mportilho.dfr.core.processor.ConditionalStatement;
import io.github.mportilho.dfr.core.processor.ConditionalStatementProcessor;
import io.github.mportilho.dfr.core.processor.LogicType;
import io.github.mportilho.dfr.core.processor.ValueExpressionResolver;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Marcelo Portilho
 */
public class ReflectionConditionalStatementProcessor implements ConditionalStatementProcessor<ReflectionParameter> {

    private static final Map<ReflectionParameter, MultiValuedMap<Annotation, List<Annotation>>> cache =
            new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.WEAK, AbstractReferenceMap.ReferenceStrength.SOFT);

    private final ValueExpressionResolver<?> valueExpressionResolver;

    public ReflectionConditionalStatementProcessor() {
        this.valueExpressionResolver = ValueExpressionResolver.EMPTY_RESOLVER;
    }

    public ReflectionConditionalStatementProcessor(ValueExpressionResolver<?> valueExpressionResolver) {
        this.valueExpressionResolver = valueExpressionResolver != null ? valueExpressionResolver : ValueExpressionResolver.EMPTY_RESOLVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConditionalStatement createConditionalStatements(ReflectionParameter reflectionParameter, Map<String, Object[]> parametersMap) {
        parametersMap = parametersMap != null ? parametersMap : Collections.emptyMap();
        List<ConditionalStatement> statements = new ArrayList<>();
        MultiValuedMap<Annotation, List<Annotation>> statementAnnotations = findStatementAnnotations(reflectionParameter);

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
        return new ConditionalStatement("conjunction_wrapper", LogicType.DISJUNCTION, false, Collections.emptyList(), statements);
    }

    static MultiValuedMap<Annotation, List<Annotation>> findStatementAnnotationsInternal(ReflectionParameter reflectionParameter) {
        MultiValuedMap<Annotation, List<Annotation>> statementAnnotations = new ArrayListValuedHashMap<>();
        for (Class<?> anInterface : getAllInterfaces(reflectionParameter.type())) {
            statementAnnotations.putAll(getAllAnnotations(anInterface));
        }
        statementAnnotations.putAll(getAllAnnotations(reflectionParameter));
        return statementAnnotations;
    }

    public static MultiValuedMap<Annotation, List<Annotation>> findStatementAnnotations(ReflectionParameter reflectionParameter) {
        return cache.computeIfAbsent(reflectionParameter, ReflectionConditionalStatementProcessor::findStatementAnnotationsInternal);
    }

    /**
     *
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
            Filter[] filters = conjunction != null ? conjunction.value() : disjunction.value();
            Statement[] statements = conjunction != null ? conjunction.disjunctions() : disjunction.conjunctions();

            String strNegate = conjunction != null ? conjunction.negate() : disjunction.negate();
            boolean negate = computeNegatingParameter(strNegate, parametersMap);

            List<FilterData> clauses = createFilterData(filters, parametersMap);
            List<ConditionalStatement> oppositeConditionals = new ArrayList<>();
            int i = 0;
            for (Statement stmt : statements) {
                List<FilterData> params = createFilterData(stmt.value(), parametersMap);
                if (!params.isEmpty()) {
                    oppositeConditionals.add(new ConditionalStatement(
                            stmtId + "_subStatements_" + i++,
                            logicType.opposite(),
                            computeNegatingParameter(stmt.negate(), parametersMap),
                            params, Collections.emptyList()));
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
     *
     */
    private Boolean computeNegatingParameter(String strNegate, Map<String, Object[]> parametersMap) {
        if ("true".equalsIgnoreCase(strNegate)) {
            return Boolean.TRUE;
        } else if ("false".equalsIgnoreCase(strNegate)) {
            return Boolean.FALSE;
        }
        Object negateParamResponse = computeValue(strNegate, "false", parametersMap);
        if (negateParamResponse != null) {
            return Boolean.parseBoolean(negateParamResponse.toString());
        }
        return Boolean.FALSE;
    }

    /**
     *
     */
    private List<FilterData> createFilterData(Filter[] filters, Map<String, Object[]> parametersMap) {
        if (filters == null || filters.length == 0) {
            return Collections.emptyList();
        }
        // fail fast
        for (Filter filter : filters) {
            validateFilter(filter);
        }

        List<FilterData> filterParameters = new ArrayList<>();
        for (Filter filter : filters) {
            List<Object[]> values = computeFilter(filter, parametersMap);
            if (values.isEmpty()) {
                if (filter.required()) {
                    throw new IllegalArgumentException(String.format("No value was found for required filter of path '%s'", filter.path()));
                }
                continue;
            }
            boolean negate = computeNegatingParameter(filter.negate(), parametersMap);

            String format = filter.format();
            Object[] formatParamValueArray = computeValue(null, format, parametersMap);
            if (formatParamValueArray != null) {
                if (formatParamValueArray.length > 1) {
                    throw new IllegalArgumentException("Attribute 'format' have more than one value for path " + filter.path());
                } else if (formatParamValueArray.length == 1) {
                    format = formatParamValueArray[0].toString();
                }
            }

            Map<String, String> modifiers = createModifierMap(filter.modifiers());

            FilterData parameter = new FilterData(filter.attributePath(), filter.path(), filter.parameters(), filter.targetType(),
                    filter.operation(), negate, filter.ignoreCase(), values, format, modifiers);
            parameter = decorateFilterData(parameter, parametersMap);
            filterParameters.add(parameter);
        }
        return filterParameters;
    }

    /**
     *
     */
    private Map<String, String> createModifierMap(String[] modifiers) {
        if (modifiers.length == 0) {
            return Collections.emptyMap();
        }
        return Arrays.stream(modifiers)
                .map(s -> {
                    String[] arr = s.split("=");
                    if (arr.length != 2) {
                        throw new IllegalArgumentException("The modifiers field format must be like 'Attribute=Value'");
                    }
                    return arr;
                }).collect(Collectors.toMap(s -> s[0], s -> s[1]));
    }

    /**
     *
     */
    private void validateFilter(Filter filter) {
        if (filter.parameters().length == 0) {
            throw new IllegalArgumentException("No parameter configured for filter of path " + filter.path());
        }

        if (IsIn.class.equals(filter.operation()) || IsNotIn.class.equals(filter.operation())) {
            if (filter.parameters().length > 1) {
                throw new IllegalArgumentException(String.format("The multi-valued operation %s cannot have more than one parameter", filter.operation().getSimpleName()));
            }
        } else {
            if (filter.constantValues().length != 0 && filter.constantValues().length != filter.parameters().length) {
                throw new IllegalStateException(String.format("Arrays of Required Parameters and Constant Values have different sizes. Parameters required: '%s'",
                        String.join(", ", Arrays.asList(filter.parameters()))));
            }
            if (filter.defaultValues().length != 0 && filter.defaultValues().length != filter.parameters().length) {
                throw new IllegalStateException(String.format("Arrays of Required Parameters and Default Values have different sizes. Parameters required: '%s'",
                        String.join(", ", Arrays.asList(filter.parameters()))));
            }
        }
    }

    /**
     *
     */
    private List<Object[]> computeFilter(Filter filter, Map<String, Object[]> parametersMap) {
        if (filter.constantValues().length > 0) {
            return computeValues(null, filter.constantValues(), parametersMap);
        }
        return computeValues(filter.parameters(), filter.defaultValues(), parametersMap);
    }

    /**
     *
     */
    private Object[] computeValue(String parameter, Object defaultValue, Map<String, Object[]> parametersMap) {
        if (parameter != null && !parameter.isBlank()) {
            return parametersMap.get(parameter);
        } else {
            Object[] values = null;
            if (defaultValue instanceof Object[] arr) {
                values = Arrays.stream(arr)
                        .mapMulti((obj, mapper) -> {
                            if (obj instanceof String str) {
                                Object[] resolvedValue = valueExpressionResolver.resolveValue(str);
                                mapper.accept(resolvedValue != null ? resolvedValue : str);
                            } else {
                                mapper.accept(obj);
                            }
                        }).toArray();
            } else if (defaultValue instanceof String strValue && !strValue.isBlank()) {
                Object[] resolvedValue = valueExpressionResolver.resolveValue(strValue);
                values = resolvedValue != null ? resolvedValue : new Object[]{strValue};
            }
            return values != null ? values : new Object[]{defaultValue};
        }
    }

    /**
     *
     */
    private List<Object[]> computeValues(String[] parameters, Object[] defaultValues, Map<String, Object[]> parametersMap) {
        if (parameters == null || parameters.length == 0) {
            return List.<Object[]>of(computeValue(null, defaultValues, parametersMap));
        }

        List<Object[]> valueList = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Object defaultValue = defaultValues != null && defaultValues.length > 0 ? defaultValues[i] : null;
            valueList.add(computeValue(parameters[i], defaultValue, parametersMap));
        }
        return valueList;

//        if (!isEmpty(parameters) && !isEmpty(defaultValues) && parameters.length == 1 && defaultValues.length == 1) {
//            values = computeValue(parameters[0], defaultValues[0], parametersMap);
//        } else if (!isEmpty(parameters) && isEmpty(defaultValues) && parameters.length == 1) {
//            values = computeValue(parameters[0], null, parametersMap);
//        } else if (isEmpty(parameters) && !isEmpty(defaultValues) && defaultValues.length == 1) {
//            values = computeValue(null, defaultValues[0], parametersMap);
//        } else if (!isEmpty(parameters) && parameters.length == 1 && !isEmpty(defaultValues) && defaultValues.length > 1) {
//            values = computeValue(parameters[0], null, parametersMap);
//            values = values == null ? new Object[]{defaultValues} : values;
//        } else if (!isEmpty(parameters)) {
//            List<Object[]> list = new ArrayList<>(parameters.length);
//            for (int i = 0; i < parameters.length; i++) {
//                Object defaultValue = defaultValues.length > 0 ? defaultValues[i] : null;
//                list.add(computeValue(parameters[i], defaultValue, parametersMap));
//            }
//            if (list.stream().allMatch(Objects::isNull)) {
//                return null;
//            }
//        }
//        return list.toArray();
    }

    /**
     *
     */
    private Object[] convertToArray(Object obj) {
        if (obj instanceof Object[] arr) {
            return arr;
        }
        return new Object[]{obj};
    }

    /**
     *
     */
    private boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     *
     */
    private static List<Class<?>> getAllInterfaces(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        List<Class<?>> interfacesFound = new ArrayList<>();
        interfacesFound.add(clazz);
        getAllInterfaces(clazz, interfacesFound);
        return interfacesFound;
    }

    /**
     *
     */
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

    /**
     *
     */
    private static Map<Annotation, List<Annotation>> getAllAnnotations(ReflectionParameter reflectionParameter) {
        if (reflectionParameter.annotations() == null || reflectionParameter.annotations().length == 0) {
            return Collections.emptyMap();
        }
        Map<Annotation, List<Annotation>> annotationsFound = new LinkedHashMap<>();
        List<Annotation> annotations = new ArrayList<>(Arrays.asList(reflectionParameter.annotations()));
        annotations.removeIf(a -> a.annotationType().getPackageName().startsWith("java.lang.annotation"));
        if (!annotations.isEmpty()) {
            VirtualAnnotationHolder virtualAnnotationHolder = new VirtualAnnotationHolder(VirtualAnnotationHolder.class, annotations);
            getAllAnnotations(virtualAnnotationHolder, annotationsFound);
        }
        return annotationsFound;
    }

    /**
     *
     */
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

    /**
     *
     */
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
