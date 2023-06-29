package ru.ibsqa.chameleon.configuration.suites;

import ru.ibsqa.chameleon.integration.ITasktrackerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuiteConfigTasktrackerImpl implements ISuiteConfig {

    @Autowired
    private ITasktrackerAdapter tasktrackerAdapter;

    @Override
    public String getSuite() {
        return tasktrackerAdapter.getSuite();
    }

    @Override
    public ConfigLevelEnum getLevel() {
        return ConfigLevelEnum.TASKTRACKER;
    }

}
