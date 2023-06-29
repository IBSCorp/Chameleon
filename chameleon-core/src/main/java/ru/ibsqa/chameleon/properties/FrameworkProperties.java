package ru.ibsqa.chameleon.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FrameworkProperties extends AbstractProperties {

    private static final String FRAMEWORK_PROPERTIES_LOCATION = "classpath:/chameleon.properties";

    @Override
    protected String getResourceLocation() {
        return FRAMEWORK_PROPERTIES_LOCATION;
    }

}
