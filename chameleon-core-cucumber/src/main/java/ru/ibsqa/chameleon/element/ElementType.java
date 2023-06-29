package ru.ibsqa.chameleon.element;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = java.lang.annotation.ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementType {
    String name() default "";

    String[] parentContexts() default {};

    boolean findGrandChildren() default false;

    String errorMessage() default "";
}
