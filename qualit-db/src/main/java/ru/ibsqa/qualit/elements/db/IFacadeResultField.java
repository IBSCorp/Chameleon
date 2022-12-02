package ru.ibsqa.qualit.elements.db;

import ru.ibsqa.qualit.db.context.IQueryObject;
import ru.ibsqa.qualit.definitions.repository.db.AbstractField;
import ru.ibsqa.qualit.elements.IFacadeReadable;

public interface IFacadeResultField extends IFacadeReadable {
    String getName();
    void initialization(IQueryObject queryObject, AbstractField metaField);
}
