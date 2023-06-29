package ru.ibsqa.chameleon.influx.context;

import lombok.Getter;
import lombok.Setter;

public class TestContext {

    @Getter
    private final String name;

    @Getter
    private final TimeInterval interval;

    @Getter
    private final InfluxEntry influxEntry;

    @Getter
    @Setter
    private String status;

    @Setter
    @Getter
    private int errCount = 0;

    @Getter
    @Setter
    private String failureStep;

    @Getter
    @Setter
    private String failureMessage = "";

    @Getter
    @Setter
    private String buildUrl = "";

    public TestContext(String name) {
        this.name = name;
        this.interval = new TimeInterval();
        this.influxEntry = new InfluxEntry();
    }
}
