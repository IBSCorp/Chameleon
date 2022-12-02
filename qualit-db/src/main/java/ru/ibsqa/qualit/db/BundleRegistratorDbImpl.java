package ru.ibsqa.qualit.db;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorDbImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "db";
    }
}
