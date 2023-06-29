package ru.ibsqa.chameleon.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DebugSteps extends AbstractSteps {

    public void evalToLog(String var) {
        log.info(var+"="+evalVariable(var));
    }

}
