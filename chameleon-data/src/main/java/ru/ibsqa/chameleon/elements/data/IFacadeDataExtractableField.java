package ru.ibsqa.chameleon.elements.data;

import ru.ibsqa.chameleon.elements.IFacade;

public interface IFacadeDataExtractableField extends IFacade {
    String getDataValue();
    String getDataPretty();
    long getDataLength();
}
