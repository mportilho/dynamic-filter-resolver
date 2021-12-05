package io.github.mportilho.dfr.core.processor.impl;

import java.lang.annotation.Annotation;

public record ReflectionParameter(Class<?> type, Annotation[] annotations) {
}
