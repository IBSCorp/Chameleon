package ru.ibsqa.chameleon.selenium.driver.configuration.options;

import org.openqa.selenium.firefox.FirefoxOptions;

public class DefaultFirefoxOptions extends FirefoxOptions {
    public FirefoxOptions setArguments(String... arguments) {
        return super.addArguments(arguments);
    }
}
