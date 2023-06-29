package ru.ibsqa.chameleon.definitions.annotations.selenium;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Page {

    String name() default "";

    String locator() default "";

    String driver() default "";

    int waitTimeOut() default -1;

    String[] frames() default {};
}