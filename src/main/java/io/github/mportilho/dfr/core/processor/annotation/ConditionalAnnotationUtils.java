package io.github.mportilho.dfr.core.processor.annotation;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class ConditionalAnnotationUtils {

    private static final Map<AnnotationProcessorParameter, MultiValuedMap<Annotation, List<Annotation>>> cache =
            new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.WEAK, AbstractReferenceMap.ReferenceStrength.SOFT);

    private ConditionalAnnotationUtils() {
    }

    /**
     *
     */
    public static MultiValuedMap<Annotation, List<Annotation>> findStatementAnnotations(AnnotationProcessorParameter annotationProcessorParameter) {
        return cache.computeIfAbsent(annotationProcessorParameter, ConditionalAnnotationUtils::findStatementAnnotationsInternal);
    }

    /**
     *
     */
    private static MultiValuedMap<Annotation, List<Annotation>> findStatementAnnotationsInternal(AnnotationProcessorParameter annotationProcessorParameter) {
        MultiValuedMap<Annotation, List<Annotation>> statementAnnotations = new ArrayListValuedHashMap<>();
        for (Class<?> anInterface : extractProcessableInterfaces(annotationProcessorParameter.type())) {
            statementAnnotations.putAll(extractFilterAnnotations(anInterface));
        }
        statementAnnotations.putAll(ConditionalAnnotationUtils.extractFilterAnnotations(annotationProcessorParameter.annotations()));
        return statementAnnotations;
    }

    /**
     * Gets the {@link Field}'s representation on the specific type
     */
    public static Field findFilterField(Class<?> clazz, String fieldName) {
        final String[] fieldNames = fieldName.split("\\.", -1);
        // if using dot notation to navigate for classes
        if (fieldNames.length > 1) {
            final String firstProperty = fieldNames[0];
            final String otherProperties = StringUtils.join(fieldNames, '.', 1, fieldNames.length);
            final Field firstPropertyType = findFilterField(clazz, firstProperty);

            Class<?> actualClass = null;
            if (!Object.class.equals(firstPropertyType.getType())) {
                if (Collection.class.isAssignableFrom(firstPropertyType.getType())) {
                    actualClass = (Class<?>) ((ParameterizedType) firstPropertyType.getGenericType()).getActualTypeArguments()[0];
                } else {
                    actualClass = firstPropertyType.getType();
                }
            }

            if (actualClass != null) {
                return findFilterField(actualClass, otherProperties);
            }
        }

        try {
            return clazz.getDeclaredField(fieldName);
        } catch (final NoSuchFieldException e) {
            if (!clazz.getSuperclass().equals(Object.class)) {
                return findFilterField(clazz.getSuperclass(), fieldName);
            }
            throw new IllegalStateException(String.format("Field '%s' does not exist in type '%s'", fieldName, clazz.getCanonicalName()));
        }
    }

    /**
     *
     */
    protected static List<Class<?>> extractProcessableInterfaces(Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptyList();
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
    protected static Map<Annotation, List<Annotation>> extractFilterAnnotations(Class<?> type) {
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
    protected static Map<Annotation, List<Annotation>> extractFilterAnnotations(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return Collections.emptyMap();
        }
        Map<Annotation, List<Annotation>> annotationsFound = new LinkedHashMap<>();
        List<Annotation> annotationList = new ArrayList<>(Arrays.asList(annotations));
        annotationList.removeIf(a -> a.annotationType().getPackageName().startsWith("java.lang.annotation"));
        if (!annotationList.isEmpty()) {
            VirtualAnnotationHolder virtualAnnotationHolder = new VirtualAnnotationHolder(VirtualAnnotationHolder.class, annotationList);
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

}
