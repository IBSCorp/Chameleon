package ru.ibsqa.qualit.definitions.repository.winium;

import ru.ibsqa.qualit.definitions.repository.selenium.AbstractMetaField;
import ru.ibsqa.qualit.elements.winium.WinElementFacade;

public abstract class AbstractMetaWiniumField extends AbstractMetaField {

    @Override
    protected String getFacadePackageName() {
        return WinElementFacade.class.getPackage().getName();
    }
}
