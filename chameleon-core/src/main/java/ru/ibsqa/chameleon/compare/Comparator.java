package ru.ibsqa.chameleon.compare;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Comparator {
    String value();
    String messageName() default "";
    ConfigurationPriority priority() default ConfigurationPriority.NORMAL;
}
