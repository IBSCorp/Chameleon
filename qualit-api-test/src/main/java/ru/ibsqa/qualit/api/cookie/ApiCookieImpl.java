package ru.ibsqa.qualit.api.cookie;

import io.restassured.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@Component
public class ApiCookieImpl implements IApiCookie {

    private List<Cookie> cookies = new ArrayList<>();

    public static final String pattern = "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)(:([^\\/]*))?((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(\\?([^#]*))?(#(.*))?$";

    @Override
    public void setCookie(Cookie cookie) {
        if (cookies.size() > 0) {
            for (Cookie cookie1 : cookies) {
                if (cookie1.getName().equals(cookie.getName()) && cookie1.getPath().equals(cookie.getPath()) &&
                        ((cookie1.getDomain() == cookie.getDomain() || (cookie1.getDomain() != null && cookie1.getDomain().equals(cookie.getDomain()))))) {
                    cookies.remove(cookie1);
                    break;
                }
            }
        }
        cookies.add(cookie);
    }

    @Override
    public Cookie getCookie(String name, String url) {
        String path = "";
        String domain = null;
        Pattern pattern = Pattern.compile(this.pattern);
        Matcher matcherUrl = pattern.matcher(url);
        if (matcherUrl.find()) {
            domain = matcherUrl.group(3);
            path = matcherUrl.group(6);
        } else fail(String.format("url %s не соответствует шаблону %s", url, this.pattern));
        for (Cookie item : cookies
        ) {
            if (item.getName().equals(name) && path.startsWith(item.getPath()) &&
                    (null == item.getDomain() || domain.endsWith(item.getDomain()))) {
                return item;
            }
        }
        log.info(String.format("Cookie с именем %s и url %s отсутствует в хранилище", name, url));
       // fail(String.format("Cookie с именем %s и url %s отсутствует в хранилище", name, url));
        return null;
    }

    @Override
    public void clearCookies() {
        cookies.clear();
    }

}
