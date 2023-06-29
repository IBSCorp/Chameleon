package ru.ibsqa.chameleon.json.context;

import ru.ibsqa.chameleon.context.IContextObject;
import ru.ibsqa.chameleon.elements.data.IFacadeDataMutableField;

import java.util.List;

public interface IJsonLookObject extends IContextObject {
    List<IFacadeDataMutableField> getFields();
    void initialize(String lookName, String jsonValue);
    void setJsonValue(String jsonValue);
}
