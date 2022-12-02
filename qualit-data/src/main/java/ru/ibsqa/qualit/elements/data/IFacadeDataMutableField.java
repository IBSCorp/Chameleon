package ru.ibsqa.qualit.elements.data;

import ru.ibsqa.qualit.elements.IFacadeClearable;
import ru.ibsqa.qualit.elements.IFacadeReadable;

public interface IFacadeDataMutableField extends IFacadeClearable, IFacadeReadable {
    void createField(String value);
    void createObject();
    void createArray();
    void deleteField();
    void addField(String value);
    void addObject();
    void addArray();
    void setObject();
    void setArray();
}
