package ru.ibsqa.chameleon.selenium.driver;

import java.util.List;

public interface IDriverManager {

    IDriverFacade getLastDriver();
    IDriverFacade getDriver(String driverId);
    List<IDriverFacade> getDrivers();
    void setCurrentDefaultDriverId(String driverId);

    void closeLastDriver();
    void closeDriver(String driverId);
    void closeAllDrivers();
}
