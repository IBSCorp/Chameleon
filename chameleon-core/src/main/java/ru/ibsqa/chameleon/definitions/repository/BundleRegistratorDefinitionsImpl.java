package ru.ibsqa.chameleon.definitions.repository;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorDefinitionsImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "definitions";
    }
}
