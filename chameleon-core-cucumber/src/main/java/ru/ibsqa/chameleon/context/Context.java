package ru.ibsqa.chameleon.context;

import ru.ibsqa.chameleon.element.ElementTypeNone;
import ru.ibsqa.chameleon.element.IElementType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Context {
    ContextChange change() default ContextChange.NONE;
    Class<? extends IElementType> delete() default ElementTypeNone.class;
    String parameter() default "";

    String value() default "";
    Class<? extends IElementType> type() default ElementTypeNone.class;
    boolean previous() default false;
    String[] variables() default {};
}

