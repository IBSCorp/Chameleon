package ru.ibsqa.qualit.json;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorJsonImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "json";
    }
}
