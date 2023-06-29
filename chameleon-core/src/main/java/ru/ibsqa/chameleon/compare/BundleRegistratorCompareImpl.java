package ru.ibsqa.chameleon.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.i18n.IBundleRegistrator;

@Component
public class BundleRegistratorCompareImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "compare";
    }
}
