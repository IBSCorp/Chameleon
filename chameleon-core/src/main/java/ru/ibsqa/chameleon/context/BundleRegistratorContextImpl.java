package ru.ibsqa.chameleon.context;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorContextImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "context";
    }
}
