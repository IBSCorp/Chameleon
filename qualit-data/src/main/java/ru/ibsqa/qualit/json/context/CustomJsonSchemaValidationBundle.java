package ru.ibsqa.qualit.json.context;

import ru.ibsqa.qualit.i18n.ILocaleManager;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.bundle.PropertiesBundle;
import com.github.fge.msgsimple.load.MessageBundleLoader;

import java.net.URL;

public class CustomJsonSchemaValidationBundle implements MessageBundleLoader {

    private static final String BUNDLE = "jschema_validation%s.properties";

    @Override
    public MessageBundle getBundle() {
        String PATH = String.format(BUNDLE, "_"+ ILocaleManager.locale().getLanguage());
        URL url = CustomJsonSchemaValidationBundle.class.getResource("/"+PATH);
        if (null == url) {
            PATH = String.format(BUNDLE, "");
        }
        return PropertiesBundle.forPath(String.format(PATH, ILocaleManager.locale().getLanguage()));
    }

}
