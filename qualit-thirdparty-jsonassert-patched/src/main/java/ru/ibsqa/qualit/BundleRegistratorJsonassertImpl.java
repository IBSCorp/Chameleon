package ru.ibsqa.qualit;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorJsonassertImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "jsonassert";
    }
}
