package ru.ibsqa.chameleon.db;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorDbImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "db";
    }
}
