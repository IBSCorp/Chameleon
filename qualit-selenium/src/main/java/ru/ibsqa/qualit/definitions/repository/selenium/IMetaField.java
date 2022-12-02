package ru.ibsqa.qualit.definitions.repository.selenium;

import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;

public interface IMetaField extends IMetaElement {
    boolean isLoaded();

    int getWaitTimeOut();

    String getCustomType();

    String getFacadeClassName(Class<? extends ISupportedDriver> supportedDriver);
}
