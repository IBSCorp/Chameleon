package ru.ibsqa.qualit.i18n;

import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LocaleManagerImpl implements ILocaleManager {

    @Value("#{ systemProperties['user.locale'] }")
    private String customLocale;

    @Autowired
    private ControlBundleCharset control;

    @Getter @Setter
    private Locale locale;

    @PostConstruct
    private void initialization() {
        if (null != customLocale) {
            locale = new Locale(customLocale);
        } else {
            locale = Locale.getDefault();
        }
        log.info(String.format("Set locale [%s]", locale.toLanguageTag()));
    }

    private List<IBundleRegistrator> bundleRegistrators;

    @Autowired
    private void collectBundleRegistrators(List<IBundleRegistrator> bundleRegistrators) {
        this.bundleRegistrators = bundleRegistrators;
    }

    private Map<String,ResourceBundle> bundlesCash = new HashMap<String,ResourceBundle>();

    @Override
    public ResourceBundle getBundle(String bundleName, Locale locale) {
        String key = bundleName;
        if (null != locale) {
            key += "_" + locale.toLanguageTag();
        }

        if (!bundlesCash.containsKey(key)) {
            ResourceBundle bundle = null;
            try {
                bundle = ResourceBundle.getBundle(bundleName, locale, control);
            } catch (MissingResourceException e) {
            }
            if (null != bundle) {
                bundlesCash.put(key, bundle);
            } else {
                return null;
            }
        }

        return bundlesCash.get(key);
    }

    @Override
    public String getMessage(String messageName) {
        return getMessage(messageName, getLocale());
    }

    @Override
    public String getMessage(String messageName, Object ... arguments) {
        return getMessage(messageName, getLocale(), arguments);
    }

    @Override
    public String getMessage(String messageName, Locale locale) {
        ResourceBundle bundle = null;
        if (null != bundleRegistrators) {
            for (IBundleRegistrator bundleRegistrator : bundleRegistrators) {
                bundle = getBundle(bundleRegistrator.getBundleName(), locale);
                if (null != bundle && bundle.containsKey(messageName)) {
                    return bundle.getString(messageName);
                }
            }
        }
        throw new MissingResourceException(
                String.format("Can't find bundle from [%s] with key [%s], locale [%s]",
                        bundleRegistrators.stream().map(item -> item.getBundleName()).collect(Collectors.joining(", ")),
                        messageName,
                        locale), null, messageName);
    }

    @Override
    public String getMessage(String messageName, Locale locale, Object... arguments) {
        return MessageFormat.format(getMessage(messageName, locale), arguments);
    }

}
