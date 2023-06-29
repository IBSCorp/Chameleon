package ru.ibsqa.chameleon;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.i18n.IBundleRegistrator;

@Component
public class BundleRegistratorWebImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "web";
    }
}
