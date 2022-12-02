package ru.ibsqa.qualit.api.context;

import ru.ibsqa.qualit.context.IContextObject;
import ru.ibsqa.qualit.elements.api.IFacadeRequestField;
import io.restassured.response.Response;

import java.util.List;

public interface IApiRequestObject extends IContextObject {
    void initialize(IApiEndpointObject endpoint, String requestName, String template);
    void send();
    Response getRawResponse();
    List<IFacadeRequestField> getFields();
}
