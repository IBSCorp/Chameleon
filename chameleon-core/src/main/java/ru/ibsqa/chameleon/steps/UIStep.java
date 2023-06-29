package ru.ibsqa.chameleon.steps;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация означает, что для данного шага будет сделан скриншот, если стоит опция делать шаги для каждого шага
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface UIStep {
}