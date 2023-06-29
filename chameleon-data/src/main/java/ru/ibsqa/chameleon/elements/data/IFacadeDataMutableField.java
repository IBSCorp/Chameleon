package ru.ibsqa.chameleon.elements.data;

import ru.ibsqa.chameleon.elements.IFacadeClearable;
import ru.ibsqa.chameleon.elements.IFacadeReadable;

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
