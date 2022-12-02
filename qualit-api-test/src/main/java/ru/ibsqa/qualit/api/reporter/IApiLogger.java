package ru.ibsqa.qualit.api.reporter;

import io.restassured.config.LogConfig;

public interface IApiLogger {
    void flush(ILogReceiver logReceiver);
    LogConfig getLogConfig();
}
