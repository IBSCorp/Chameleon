package ru.ibsqa.chameleon.steps;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TestStep {
    String value() default "";
}
