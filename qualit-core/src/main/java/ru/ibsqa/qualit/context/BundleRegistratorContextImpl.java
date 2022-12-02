package ru.ibsqa.qualit.context;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorContextImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "context";
    }
}
