package ru.ibsqa.chameleon.elements;

import ru.ibsqa.chameleon.context.IContextObject;
import org.json.JSONArray;

public interface IFacadeCollection<CONTEXT extends IContextObject> extends IFacade, IFacadeExportToJson<JSONArray>, Iterable<CONTEXT> {
}
