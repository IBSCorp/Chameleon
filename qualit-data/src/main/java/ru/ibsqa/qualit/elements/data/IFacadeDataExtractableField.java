package ru.ibsqa.qualit.elements.data;

import ru.ibsqa.qualit.elements.IFacade;

public interface IFacadeDataExtractableField extends IFacade {
    String getDataValue();
    String getDataPretty();
    long getDataLength();
}
