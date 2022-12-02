package ru.ibsqa.qualit.configuration.suite;

import ru.ibsqa.qualit.configuration.suites.ConfigLevelEnum;
import ru.ibsqa.qualit.configuration.suites.ISuiteConfig;
import lombok.Getter;
import lombok.Setter;

public class SuiteConfigFileImpl implements ISuiteConfig {

    @Getter @Setter
    private String suite;

    @Override
    public ConfigLevelEnum getLevel() {
        return ConfigLevelEnum.CONFIG_FILE;
    }
}
