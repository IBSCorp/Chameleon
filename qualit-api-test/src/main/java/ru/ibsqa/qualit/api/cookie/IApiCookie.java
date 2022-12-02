package ru.ibsqa.qualit.api.cookie;

import ru.ibsqa.qualit.api.context.IApiResponseObject;
import ru.ibsqa.qualit.context.IContextManager;
import io.restassured.http.Cookie;

public interface IApiCookie extends IContextManager<IApiResponseObject> {
    void setCookie(Cookie cookie);
    Cookie getCookie(String name, String url);
    void clearCookies();
}
