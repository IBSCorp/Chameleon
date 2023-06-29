package ru.ibsqa.chameleon.evaluate;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Evaluator {
    String[] value();
    ConfigurationPriority priority() default ConfigurationPriority.NORMAL;
}
