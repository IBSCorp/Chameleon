package ru.ibsqa.chameleon.influx;

import io.cucumber.java.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Этот класс ловит события начала/окончания теста и передает управление в реализацию IInfluxManager.
 * Если стандартная функциональность InfluxManagerImpl не подходит, то бин, имплементирующий интерфейс
 * IInfluxManager может быть определен разработчиком автотестов в конкретном проекте с пометкой @Primary
 */
public class InfluxFeatureHooks {

    @Autowired
    private IInfluxManager influxManager;

    @Before
    public void start(Scenario scenario) {
        influxManager.startTest(scenario.getName());
    }

    @After
    public void end(Scenario scenario) {
        influxManager.endTest(scenario.getStatus().name().toLowerCase());
    }

}
