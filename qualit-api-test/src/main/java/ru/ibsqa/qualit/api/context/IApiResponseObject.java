package ru.ibsqa.qualit.api.context;

import ru.ibsqa.qualit.context.IContextObject;
import ru.ibsqa.qualit.elements.api.IFacadeResponseField;
import ru.ibsqa.qualit.json.context.wrapper.IDataWrapper;

import java.util.List;

public interface IApiResponseObject extends IContextObject {
    void initialize(IApiEndpointObject endpoint, String responseName);
    void validateByMeta();
    default void validateStatusCode(int expectedStatusCode) {
        validateStatusCodes(expectedStatusCode);
    }
    void validateStatusCodes(int... expectedStatusCodes);
    void validateSchema(String schema);
    List<IFacadeResponseField> getFields();
    IDataWrapper getDataWrapper();
    io.restassured.response.Response getResponse();
}
