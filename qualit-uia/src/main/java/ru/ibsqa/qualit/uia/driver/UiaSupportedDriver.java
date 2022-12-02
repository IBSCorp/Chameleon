package ru.ibsqa.qualit.uia.driver;

import org.openqa.selenium.WebDriver;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;

public enum UiaSupportedDriver implements ISupportedDriver {

    UI_AUTOMATION(UiaDriver.class);

    private final Class<? extends WebDriver> _class;
    private final String _item;

    UiaSupportedDriver(Class<? extends WebDriver> __class, String item) {
        this._class = __class;
        this._item = item;
    }

    UiaSupportedDriver(Class<? extends WebDriver> __class) {
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
            case UI_AUTOMATION:
                break;

        }
    }
}