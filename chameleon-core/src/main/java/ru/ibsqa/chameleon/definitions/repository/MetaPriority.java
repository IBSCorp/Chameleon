package ru.ibsqa.chameleon.definitions.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface MetaPriority {
    ConfigurationPriority value() default ConfigurationPriority.NORMAL;
}
