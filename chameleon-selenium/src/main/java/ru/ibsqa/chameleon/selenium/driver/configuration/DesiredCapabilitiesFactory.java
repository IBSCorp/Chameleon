package ru.ibsqa.chameleon.selenium.driver.configuration;

import ru.ibsqa.chameleon.selenium.driver.configuration.options.DefaultChromeOptions;
import ru.ibsqa.chameleon.selenium.driver.configuration.options.DefaultFirefoxOptions;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DesiredCapabilitiesFactory {

    public DesiredCapabilities setOptions(MutableCapabilities mutableCapabilities) {
        if (mutableCapabilities instanceof DefaultChromeOptions) {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, mutableCapabilities);
            return desiredCapabilities;
        } else if (mutableCapabilities instanceof DefaultFirefoxOptions) {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, mutableCapabilities);
            return desiredCapabilities;
        }
        return null;
    }
}
