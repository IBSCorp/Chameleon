package ru.ibsqa.chameleon.definitions.annotations.selenium;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
// Не используется??
public @interface Collection {

    String names() default "";

    String locator() default "";

    int waitTimeOut() default -1;

    String[] frames() default {};
}