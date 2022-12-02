package ru.ibsqa.qualit.sap.elements;

import ru.ibsqa.qualit.elements.selenium.IFacadeSelenium;
import com.jacob.com.Variant;

public interface ISapElement extends IFacadeSelenium {
    Variant getVariant();

}
