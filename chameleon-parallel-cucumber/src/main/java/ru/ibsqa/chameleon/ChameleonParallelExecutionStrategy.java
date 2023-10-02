package ru.ibsqa.chameleon;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

@Slf4j
public class ChameleonParallelExecutionStrategy implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {

    private boolean parallelAuto = Boolean.parseBoolean(System.getProperty("parallel.auto", "false"));
    private int parallelCount = Integer.parseInt(System.getProperty("parallel.count", "2"));
    private static boolean firstCall = true;

    protected synchronized void logInfo() {
        if (firstCall) {
            log.info(String.format("Количество потоков параллелизации тестов: %d", getCount()));
            firstCall = false;
        }
    }

    protected int getCount() {
        return parallelAuto ? Runtime.getRuntime().availableProcessors() : parallelCount;
    }

    @Override
    public int getParallelism() {
        return getCount();
    }

    @Override
    public int getMinimumRunnable() {
        return 0;
    }

    @Override
    public int getMaxPoolSize() {
        return getCount();
    }

    @Override
    public int getCorePoolSize() {
        return getCount();
    }

    @Override
    public int getKeepAliveSeconds() {
        return 60;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        logInfo();
        return this;
    }
}
