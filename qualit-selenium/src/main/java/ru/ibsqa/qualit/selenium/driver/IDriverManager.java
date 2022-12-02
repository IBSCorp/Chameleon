package ru.ibsqa.qualit.selenium.driver;

import java.util.List;

public interface IDriverManager {

    WebDriverFacade getLastDriver();
    WebDriverFacade getDriver(String driverId);
    List<WebDriverFacade> getDrivers();
    void setCurrentDefaultDriverId(String driverId);

    void closeLastDriver();
    void closeDriver(String driverId);
    void closeAllDrivers();
}
