package io.github.mportilho.dfr.core.processor.annotation;

import java.lang.annotation.Annotation;

public record AnnotationProcessorParameter(Class<?> type, Annotation[] annotations) {
}
