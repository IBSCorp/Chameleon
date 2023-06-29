package ru.ibsqa.chameleon.elements;

import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaElement;
import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface MetaElement {
    Class<? extends IMetaElement> value();
    Class<? extends ISupportedDriver> supportedDriver() default ISupportedDriver.class;
    ConfigurationPriority priority() default ConfigurationPriority.NORMAL;
}
