package ru.ibsqa.qualit.elements;

import ru.ibsqa.qualit.context.IContextObject;
import org.json.JSONArray;

public interface IFacadeCollection<CONTEXT extends IContextObject> extends IFacade, IFacadeExportToJson<JSONArray>, Iterable<CONTEXT> {
}
