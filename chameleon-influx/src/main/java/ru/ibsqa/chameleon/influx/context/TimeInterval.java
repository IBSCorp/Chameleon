package ru.ibsqa.chameleon.influx.context;

import lombok.Getter;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class TimeInterval {

    @Getter
    private long startTime;

    @Getter
    private long endTime;

    public long getDuration() {
        return this.endTime - this.startTime;
    }

    private final TimeUnit timeUnit;
    private final Function<Long, Long> timeSource;

    public TimeInterval() {
        this(TimeUnit.MILLISECONDS, start -> System.currentTimeMillis());
    }

    public TimeInterval(TimeUnit timeUnit, Function<Long, Long> timeSource) {
        this.timeUnit = timeUnit;
        this.timeSource = timeSource;
    }

    public void start() {
        this.startTime = timeSource.apply(startTime);
    }

    public void end() {
        this.endTime = timeSource.apply(startTime);
    }

}
