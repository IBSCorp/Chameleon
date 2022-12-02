package ru.ibsqa.qualit.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Context {
    ContextChange change() default ContextChange.NONE;
    ContextType delete() default ContextType.NONE;
    String parameter() default "";

    String value() default "";
    ContextType type() default ContextType.NONE;
    boolean previous() default false;
    boolean onlyStepContext() default false;
    String[] variables() default {};
}

