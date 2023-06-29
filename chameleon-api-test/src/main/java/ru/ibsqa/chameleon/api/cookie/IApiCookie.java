package ru.ibsqa.chameleon.api.cookie;

import ru.ibsqa.chameleon.api.context.IApiResponseObject;
import ru.ibsqa.chameleon.context.IContextManager;
import io.restassured.http.Cookie;

public interface IApiCookie extends IContextManager<IApiResponseObject> {
    void setCookie(Cookie cookie);
    Cookie getCookie(String name, String url);
    void clearCookies();
}
