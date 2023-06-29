package ru.ibsqa.chameleon.definitions.repository.selenium;

import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;

public interface IMetaField extends IMetaElement {
    boolean isLoaded();

    int getWaitTimeOut();

    String getCustomType();

    String getFacadeClassName(Class<? extends ISupportedDriver> supportedDriver);
}
