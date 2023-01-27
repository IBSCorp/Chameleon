package ru.ibsqa.qualit.sap.driver;

import lombok.AllArgsConstructor;
import org.openqa.selenium.WebDriver;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;

@AllArgsConstructor
public enum SapSupportedDriver implements ISupportedDriver {

    SAP(SapDriver.class);

    private final Class<? extends WebDriver> driverClass;

    @Override
    public Class<? extends WebDriver> getAsClass() {
        return driverClass;
    }

    @Override
    public void initDriver(IDriverConfiguration configuration) {
        System.setProperty(com.jacob.com.LibraryLoader.JACOB_DLL_PATH, System.getProperty("user.dir") + "\\" + configuration.getDriverPath());
    }
}
