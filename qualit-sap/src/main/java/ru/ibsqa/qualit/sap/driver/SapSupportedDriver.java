package ru.ibsqa.qualit.sap.driver;


import org.openqa.selenium.WebDriver;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;


public enum SapSupportedDriver implements ISupportedDriver {

    SAP(SapDriver.class);

    private final Class<? extends WebDriver> _class;
    private final String _item;

    SapSupportedDriver(Class<? extends WebDriver> __class, String item) {
        this._class = __class;
        this._item = item;
    }

    SapSupportedDriver(Class<? extends WebDriver> __class) {
        this._class = __class;
        this._item = null;
    }

    public Class<? extends WebDriver> getAsClass() {
        return _class;
    }

    public String getItem() {
        return _item;
    }

    public void initDriver(IDriverConfiguration configuration) {
        switch (this) {
            case SAP:
                System.setProperty(com.jacob.com.LibraryLoader.JACOB_DLL_PATH, System.getProperty("user.dir") + "\\" + configuration.getDriverPath());
                break;

        }
    }
}
