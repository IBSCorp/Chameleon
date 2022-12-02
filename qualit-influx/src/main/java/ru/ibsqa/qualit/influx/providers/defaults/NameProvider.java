package ru.ibsqa.qualit.influx.providers.defaults;

import ru.ibsqa.qualit.influx.config.InfluxConfig;
import ru.ibsqa.qualit.influx.context.TestContext;
import ru.ibsqa.qualit.influx.providers.FieldData;
import ru.ibsqa.qualit.influx.providers.IFieldProvider;
import ru.ibsqa.qualit.influx.providers.Moment;
import org.springframework.stereotype.Component;

@Component
public class NameProvider implements IFieldProvider {
    @Override
    public FieldData getFieldData(InfluxConfig config, Moment moment, TestContext testContext) {
        if (Moment.BEFORE_TEST == moment && !config.isSkipDefaultProviders()) {
            return new FieldData("name", testContext.getName());
        }
        return null;
    }
}
