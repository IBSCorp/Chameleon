package ru.ibsqa.qualit.image.driver;


import org.openqa.selenium.WebDriver;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.selenium.driver.configuration.IDriverConfiguration;


public enum ImageSupportedDriver implements ISupportedDriver {

    IMAGE_DRIVER(ImageDriver.class);

    private final Class<? extends WebDriver> _class;
    private final String _item;

    ImageSupportedDriver(Class<? extends WebDriver> __class, String item) {
        this._class = __class;
        this._item = item;
    }

    ImageSupportedDriver(Class<? extends WebDriver> __class) {
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
            case IMAGE_DRIVER:
                break;

        }
    }
}
