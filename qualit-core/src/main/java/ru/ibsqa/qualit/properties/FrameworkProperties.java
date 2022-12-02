package ru.ibsqa.qualit.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FrameworkProperties extends AbstractProperties {

    private static final String FRAMEWORK_PROPERTIES_LOCATION = "classpath:/qualit.properties";

    @Override
    protected String getResourceLocation() {
        return FRAMEWORK_PROPERTIES_LOCATION;
    }

}
