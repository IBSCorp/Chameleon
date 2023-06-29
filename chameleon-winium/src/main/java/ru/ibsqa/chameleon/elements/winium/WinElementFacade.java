package ru.ibsqa.chameleon.elements.winium;

import ru.ibsqa.chameleon.elements.selenium.WebElementFacade;

public abstract class WinElementFacade extends WebElementFacade {

    @Override
    public String getFieldValue() {
        return getWrappedElement().getAttribute("Name").replaceAll("\\u00A0", " ");
    }

}
