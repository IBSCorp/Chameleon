package ru.ibsqa.qualit;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AllureConfiguration {
    private boolean enabled = Boolean.parseBoolean(System.getProperty("allure.enabled", "true"));
}
