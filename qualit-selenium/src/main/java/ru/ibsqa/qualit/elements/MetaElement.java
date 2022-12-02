package ru.ibsqa.qualit.elements;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaElement;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;

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
