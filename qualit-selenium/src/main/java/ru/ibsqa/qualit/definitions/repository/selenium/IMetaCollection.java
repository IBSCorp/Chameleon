package ru.ibsqa.qualit.definitions.repository.selenium;

import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;

public interface IMetaCollection extends IMetaContainer {
    String getCustomType();

    String getFacadeClassName(Class<? extends ISupportedDriver> supportedDriver);
}
