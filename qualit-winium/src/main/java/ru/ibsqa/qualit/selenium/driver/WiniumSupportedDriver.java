package ru.ibsqa.qualit.selenium.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.winium.WiniumDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;

public enum WiniumSupportedDriver implements ISupportedDriver {

    WINIUM(WiniumDriver.class);

    private final Class<? extends WebDriver> _class;
    private final String _item;

    WiniumSupportedDriver(Class<? extends WebDriver> __class, String item) {
        this._class = __class;
        this._item = item;
    }

    WiniumSupportedDriver(Class<? extends WebDriver> __class) {
        this._class = __class;
        this._item = null;
    }

    @Override
    public Class<? extends WebDriver> getAsClass() {
        return _class;
    }

    public String getItem() {
        return _item;
    }

    @Override
    public void initDriver(IDriverConfiguration configuration) {
    }

}
