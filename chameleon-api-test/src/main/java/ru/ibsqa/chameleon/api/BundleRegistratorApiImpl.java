package ru.ibsqa.chameleon.api;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorApiImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "rest";
    }
}
