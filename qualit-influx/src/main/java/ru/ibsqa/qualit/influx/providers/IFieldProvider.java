package ru.ibsqa.qualit.influx.providers;

import ru.ibsqa.qualit.influx.config.InfluxConfig;
import ru.ibsqa.qualit.influx.context.TestContext;

public interface IFieldProvider {
    FieldData getFieldData(InfluxConfig config, Moment moment, TestContext context);
}
