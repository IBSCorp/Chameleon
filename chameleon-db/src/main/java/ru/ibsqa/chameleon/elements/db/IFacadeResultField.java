package ru.ibsqa.chameleon.elements.db;

import ru.ibsqa.chameleon.db.context.IQueryObject;
import ru.ibsqa.chameleon.definitions.repository.db.AbstractField;
import ru.ibsqa.chameleon.elements.IFacadeReadable;

public interface IFacadeResultField extends IFacadeReadable {
    String getName();
    void initialization(IQueryObject queryObject, AbstractField metaField);
}
