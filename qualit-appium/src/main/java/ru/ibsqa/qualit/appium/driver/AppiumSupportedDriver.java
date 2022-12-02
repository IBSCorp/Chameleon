package ru.ibsqa.qualit.appium.driver;


import org.openqa.selenium.WebDriver;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public enum AppiumSupportedDriver implements ISupportedDriver {

    IOS(IOSDriver.class),
    ANDROID(AndroidDriver.class),
    OTHER(AndroidDriver.class);

    private final Class<? extends WebDriver> _class;
    private final String _item;

    AppiumSupportedDriver(Class<? extends WebDriver> __class, String item) {
        this._class = __class;
        this._item = item;
    }

    AppiumSupportedDriver(Class<? extends WebDriver> __class) {
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
            case ANDROID:
                break;
            case IOS:
                break;
            case OTHER:
                break;
        }
    }
}
