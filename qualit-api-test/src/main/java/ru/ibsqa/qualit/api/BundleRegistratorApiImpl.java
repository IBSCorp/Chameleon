package ru.ibsqa.qualit.api;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorApiImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "rest";
    }
}
