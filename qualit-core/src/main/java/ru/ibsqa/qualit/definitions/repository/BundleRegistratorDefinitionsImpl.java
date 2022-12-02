package ru.ibsqa.qualit.definitions.repository;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorDefinitionsImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "definitions";
    }
}
