package ru.ibsqa.chameleon.api.reporter;

import io.restassured.config.LogConfig;

public interface IApiLogger {
    void flush(ILogReceiver logReceiver);
    LogConfig getLogConfig();
}
