package ru.ibsqa.qualit.selenium;

import ru.ibsqa.qualit.i18n.IBundleRegistrator;
import org.springframework.stereotype.Component;

@Component
public class BundleRegistratorSeleniumImpl implements IBundleRegistrator {
    @Override
    public String getBundleName() {
        return "selenium";
    }
}
