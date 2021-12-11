package io.github.mportilho.dfr.core.processor.annotation;

import java.lang.annotation.Annotation;
import java.util.List;

class VirtualAnnotationHolder implements Annotation {

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
