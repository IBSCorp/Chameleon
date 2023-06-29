package ru.ibsqa.chameleon.sap.elements;

import ru.ibsqa.chameleon.elements.selenium.IFacadeSelenium;
import com.jacob.com.Variant;

public interface ISapElement extends IFacadeSelenium {
    Variant getVariant();

}
