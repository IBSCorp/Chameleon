package ru.ibsqa.chameleon.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AllureProperties extends AbstractProperties {

    private static final String ALLURE_PROPERTIES_LOCATION = "classpath:/allure.properties";
    private static final String ALLURE_RESULT_DIRECTORY = "allure.results.directory";
    @Override
    protected String getResourceLocation() {
        return ALLURE_PROPERTIES_LOCATION;
    }

    public String getResultsDirectory() {
        return getProperty(ALLURE_RESULT_DIRECTORY, "target/allure-results", false);
    }
}
