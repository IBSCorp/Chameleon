package ru.ibsqa.chameleon.logger;

import ru.ibsqa.chameleon.utils.spring.SpringUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Objects;

public class ScreenAppender extends AppenderSkeleton {

    private ILogRender logRender;

    private ILogRender getLogRender() {
        if (Objects.isNull(logRender)) {
            try {
                logRender = SpringUtils.getBean(ILogRender.class);
            } catch (Exception e) {
            }
        }
        return logRender;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        ILogRender logDestination = getLogRender();
        if (Objects.nonNull(logDestination) && Objects.nonNull(loggingEvent) && Objects.nonNull(loggingEvent.getMessage())) {
            boolean isError = false;
            if (Objects.nonNull(loggingEvent.getLevel()) && loggingEvent.getLevel().isGreaterOrEqual(Level.ERROR)) {
                isError = true;
            }
            logDestination.println(isError, loggingEvent.getMessage().toString());
        }
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
