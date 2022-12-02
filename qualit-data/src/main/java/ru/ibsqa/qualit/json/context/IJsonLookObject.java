package ru.ibsqa.qualit.json.context;

import ru.ibsqa.qualit.context.IContextObject;
import ru.ibsqa.qualit.elements.data.IFacadeDataMutableField;

import java.util.List;

public interface IJsonLookObject extends IContextObject {
    List<IFacadeDataMutableField> getFields();
    void initialize(String lookName, String jsonValue);
    void setJsonValue(String jsonValue);
}
