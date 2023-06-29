package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorWindowsImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "windows";
    }
}
