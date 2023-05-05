package ru.ibsqa.qualit.checker.loglevel;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.checker.IStartChecker;
import ru.ibsqa.qualit.checker.StartCheckerPriority;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

import java.util.Objects;

@Component
@Slf4j
public class LoglevelChecker implements IStartChecker {

    @Override
    public StartCheckerPriority getPriority() {
        return StartCheckerPriority.HIGH;
    }

    @Override
    public void check() {
        String loglevel = System.getProperties().getProperty("loglevel", null);
        if (Objects.nonNull(loglevel)) {
            var level = Level.toLevel(loglevel);
            LogManager.getRootLogger().setLevel(level);
            if (!level.isGreaterOrEqual(Level.INFO)) {
                SpringUtils.setDebug(true);
            }
        }
    }
}
