package ru.ibsqa.chameleon.configuration.suite;

import ru.ibsqa.chameleon.configuration.suites.ConfigLevelEnum;
import ru.ibsqa.chameleon.configuration.suites.ISuiteConfig;
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
