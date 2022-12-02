package ru.ibsqa.qualit.influx.providers.defaults;

import ru.ibsqa.qualit.influx.config.InfluxConfig;
import ru.ibsqa.qualit.influx.context.TestContext;
import ru.ibsqa.qualit.influx.providers.ITagProvider;
import ru.ibsqa.qualit.influx.providers.Moment;
import ru.ibsqa.qualit.influx.providers.TagData;
import org.springframework.stereotype.Component;

@Component
public class EmptyTagProvider implements ITagProvider {
    @Override
    public TagData getTagData(InfluxConfig config, Moment moment, TestContext testContext) {
        return null;
    }
}
