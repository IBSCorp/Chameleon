package ru.ibsqa.chameleon.definitions.repository.winium;

import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaField;
import ru.ibsqa.chameleon.elements.winium.WinElementFacade;

public abstract class AbstractMetaWiniumField extends AbstractMetaField {

    @Override
    protected String getFacadePackageName() {
        return WinElementFacade.class.getPackage().getName();
    }
}
