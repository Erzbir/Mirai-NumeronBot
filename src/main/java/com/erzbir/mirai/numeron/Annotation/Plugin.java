package com.erzbir.mirai.numeron.Annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Erzbir
 * @Date: 2022/11/16 21:39
 */
@Component
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.TYPE)
public @interface Plugin {
}
