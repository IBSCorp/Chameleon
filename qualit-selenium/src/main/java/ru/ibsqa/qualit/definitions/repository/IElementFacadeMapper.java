package ru.ibsqa.qualit.definitions.repository;

import ru.ibsqa.qualit.definitions.repository.selenium.IMetaElement;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;

public interface IElementFacadeMapper {
    String getFacadeClassName(Class<? extends IMetaElement> metaClass, Class<? extends ISupportedDriver> supportedDriver);
}
