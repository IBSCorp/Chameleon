package ru.ibsqa.chameleon.influx.providers;

import ru.ibsqa.chameleon.influx.config.InfluxConfig;
import ru.ibsqa.chameleon.influx.context.TestContext;

public interface ITagProvider {
    TagData getTagData(InfluxConfig config, Moment moment, TestContext context);
}