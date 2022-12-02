package ru.ibsqa.qualit.configuration.suite;

import ru.ibsqa.qualit.configuration.suites.ConfigLevelEnum;
import ru.ibsqa.qualit.configuration.suites.ISuiteConfig;
import org.springframework.stereotype.Component;

/**
 * Получение suite из параметра командной строки
 */
@Component
public class SuiteConfigCommandLineImpl implements ISuiteConfig {

    @Override
    public String getSuite() {
        return System.getProperty("suite");
    }

    @Override
    public ConfigLevelEnum getLevel() {
        return ConfigLevelEnum.COMMAND_LINE;
    }

}
