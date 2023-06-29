package ru.ibsqa.chameleon.influx.providers.defaults;

import ru.ibsqa.chameleon.influx.config.InfluxConfig;
import ru.ibsqa.chameleon.influx.context.TestContext;
import ru.ibsqa.chameleon.influx.providers.FieldData;
import ru.ibsqa.chameleon.influx.providers.IFieldProvider;
import ru.ibsqa.chameleon.influx.providers.Moment;
import org.springframework.stereotype.Component;

@Component
public class StepNameProvider implements IFieldProvider {

    @Override
    public FieldData getFieldData(InfluxConfig config, Moment moment, TestContext context) {
        if (Moment.BEFORE_STEP == moment && !config.isSkipDefaultProviders()) {
            return new FieldData("name", context.getName());
        }
        return null;
    }
}
