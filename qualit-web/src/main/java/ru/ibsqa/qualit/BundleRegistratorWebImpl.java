package ru.ibsqa.qualit;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.i18n.IBundleRegistrator;

@Component
public class BundleRegistratorWebImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "web";
    }
}
