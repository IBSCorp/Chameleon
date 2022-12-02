package ru.ibsqa.qualit.definitions.repository.selenium;

import java.util.List;

public interface IMetaContainer extends IMetaElement {
    List<IMetaElement> getElements();

    List<IMetaField> getFields();

    int getWaitTimeOut();
}
