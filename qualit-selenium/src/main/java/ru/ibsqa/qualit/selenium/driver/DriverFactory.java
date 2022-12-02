package ru.ibsqa.qualit.selenium.driver;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface DriverFactory {
    ConfigurationPriority priority() default ConfigurationPriority.HIGH;
}
