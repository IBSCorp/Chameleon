package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.utils.commandpromt.ICommandPrompt;
import ru.ibsqa.qualit.utils.os.OsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.fail;

@Component
public class WinProcessSteps extends AbstractSteps {

    @Autowired
    private ICommandPrompt commandPrompt;

    @TestStep("завершен процесс \"${process}\"")
    public void killProcess(String process) {
        String processName = process;
        if (null == processName || processName.isEmpty()) {
            fail(message("wrongProcessNameAssertMessage"));
        }
        switch (OsUtils.getOperatingSystemType()) {
            case Windows:
            case WindowsServer:
                commandPrompt.runCommand(String.format("taskkill /F /IM \"%s\"", processName));
                break;
            default:
                fail(message("wrongOsTypeAssertMessage"));
                break;
        }

    }

}
