package ru.ibsqa.chameleon;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorJsonassertImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "jsonassert";
    }
}
