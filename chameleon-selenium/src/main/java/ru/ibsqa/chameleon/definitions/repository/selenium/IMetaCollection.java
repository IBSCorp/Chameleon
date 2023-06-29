package ru.ibsqa.chameleon.definitions.repository.selenium;

import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;

public interface IMetaCollection extends IMetaContainer {
    String getCustomType();

    String getFacadeClassName(Class<? extends ISupportedDriver> supportedDriver);
}
