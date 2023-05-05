package ru.ibsqa.qualit.compare;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.i18n.IBundleRegistrator;

@Component
public class BundleRegistratorCompareImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "compare";
    }
}
