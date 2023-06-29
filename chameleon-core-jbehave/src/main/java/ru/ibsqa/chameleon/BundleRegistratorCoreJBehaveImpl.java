package ru.ibsqa.chameleon;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorCoreJBehaveImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "core-jbehave";
    }
}
