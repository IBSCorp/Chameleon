package ru.ibsqa.qualit.evaluate;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

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
