package ru.ibsqa.chameleon.influx.providers.defaults;

import ru.ibsqa.chameleon.influx.config.InfluxConfig;
import ru.ibsqa.chameleon.influx.context.TestContext;
import ru.ibsqa.chameleon.influx.providers.ITagProvider;
import ru.ibsqa.chameleon.influx.providers.Moment;
import ru.ibsqa.chameleon.influx.providers.TagData;
import org.springframework.stereotype.Component;

@Component
public class EmptyTagProvider implements ITagProvider {
    @Override
    public TagData getTagData(InfluxConfig config, Moment moment, TestContext testContext) {
        return null;
    }
}
