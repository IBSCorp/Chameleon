package ru.ibsqa.chameleon.definitions.repository;

import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaElement;
import ru.ibsqa.chameleon.selenium.driver.ISupportedDriver;

public interface IElementFacadeMapper {
    String getFacadeClassName(Class<? extends IMetaElement> metaClass, Class<? extends ISupportedDriver> supportedDriver);
}
