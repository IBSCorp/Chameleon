package ru.ibsqa.chameleon.json;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorJsonImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "json";
    }
}
