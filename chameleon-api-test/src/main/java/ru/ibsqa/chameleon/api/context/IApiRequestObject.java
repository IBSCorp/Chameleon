package ru.ibsqa.chameleon.api.context;

import ru.ibsqa.chameleon.context.IContextObject;
import ru.ibsqa.chameleon.elements.api.IFacadeRequestField;
import io.restassured.response.Response;

import java.util.List;

public interface IApiRequestObject extends IContextObject {
    void initialize(IApiEndpointObject endpoint, String requestName, String template);
    void send();
    Response getRawResponse();
    List<IFacadeRequestField> getFields();
}
