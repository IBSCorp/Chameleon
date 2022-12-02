package ru.ibsqa.qualit.selenium.driver.configuration.options;

import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class DefaultChromeOptions extends ChromeOptions {
    public ChromeOptions setArguments(String... arguments) {
        return super.addArguments(arguments);
    }

    public ChromeOptions setExtensions(File... paths) {
        return super.addExtensions(paths);
    }
}
