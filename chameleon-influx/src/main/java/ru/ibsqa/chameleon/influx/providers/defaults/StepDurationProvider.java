package ru.ibsqa.chameleon.influx.providers.defaults;

import ru.ibsqa.chameleon.influx.config.InfluxConfig;
import ru.ibsqa.chameleon.influx.context.TestContext;
import ru.ibsqa.chameleon.influx.providers.FieldData;
import ru.ibsqa.chameleon.influx.providers.IFieldProvider;
import ru.ibsqa.chameleon.influx.providers.Moment;
import org.springframework.stereotype.Component;

@Component
public class StepDurationProvider implements IFieldProvider {

    public FieldData getFieldData(InfluxConfig config, Moment moment, TestContext context) {
        if (Moment.AFTER_STEP == moment && !config.isSkipDefaultProviders()) {
            return new FieldData("duration", context.getInterval().getDuration());
        }
        return null;
    }
}
