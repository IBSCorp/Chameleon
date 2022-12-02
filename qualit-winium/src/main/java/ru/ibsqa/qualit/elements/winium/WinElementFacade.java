package ru.ibsqa.qualit.elements.winium;

import ru.ibsqa.qualit.elements.selenium.WebElementFacade;

public abstract class WinElementFacade extends WebElementFacade {

    @Override
    public String getFieldValue() {
        return getWrappedElement().getAttribute("Name").replaceAll("\\u00A0", " ");
    }

}
