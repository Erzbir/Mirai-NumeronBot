package com.erzbir.numeron.core.parser;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Erzbir
 * @Date 2023/7/27
 */
public interface AnnotationParser {
    void inject(Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
