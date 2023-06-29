package ru.ibsqa.chameleon.i18n;

import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.util.Locale;
import java.util.ResourceBundle;

public interface ILocaleManager {

    void setLocale(Locale locale);
    Locale getLocale();

    ResourceBundle getBundle(String bundleName, Locale locale);

    default ResourceBundle getBundle(String bundleName) {
        return getBundle(bundleName,getLocale());
    }

    String getMessage(String messageName);
    String getMessage(String messageName, Object ... arguments);
    String getMessage(String messageName, Locale locale);
    String getMessage(String messageName, Locale locale, Object ... arguments);

    static Locale locale() {
        return get().getLocale();
    }

    static ResourceBundle bundle(String bundleName, Locale locale) {
        return get().getBundle(bundleName, locale);
    }

    static ResourceBundle bundle(String bundleName) {
        return get().getBundle(bundleName);
    }

    static ILocaleManager get() {
        return SpringUtils.getBean(ILocaleManager.class);
    }

    static String message(String messageName) {
        return get().getMessage(messageName);
    }

    static String message(String messageName, Object ... arguments) {
        return get().getMessage(messageName, arguments);
    }

    static String message(String messageName, Locale locale)  {
        return get().getMessage(messageName, locale);
    }

    static String message(String messageName, Locale locale, Object ... arguments)  {
        return get().getMessage(messageName, locale, arguments);
    }
}
