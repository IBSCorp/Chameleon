package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorWindowsImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "windows";
    }
}
