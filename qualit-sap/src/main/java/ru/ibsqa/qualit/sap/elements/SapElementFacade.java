package ru.ibsqa.qualit.sap.elements;

import ru.ibsqa.qualit.elements.selenium.WebElementFacade;
import com.jacob.com.Variant;

public abstract class SapElementFacade extends WebElementFacade implements ISapElement {

    public ISapElement getSapElement() {
        return (ISapElement)getWrappedElement();
    };

    @Override
    public Variant getVariant() {
        return getSapElement().getVariant();
    }
}
