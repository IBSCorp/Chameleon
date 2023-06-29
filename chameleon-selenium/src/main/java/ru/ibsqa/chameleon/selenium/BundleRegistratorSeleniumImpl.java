package ru.ibsqa.chameleon.selenium;

import ru.ibsqa.chameleon.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorSeleniumImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "selenium";
    }
}
