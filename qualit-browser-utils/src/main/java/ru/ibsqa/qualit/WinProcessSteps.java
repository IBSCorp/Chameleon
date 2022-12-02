package ru.ibsqa.qualit;

import ru.ibsqa.qualit.steps.AbstractSteps;
import ru.ibsqa.qualit.utils.commandpromt.ICommandPrompt;
import ru.ibsqa.qualit.utils.os.OsUtils;
import ru.ibsqa.qualit.steps.TestStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

@Component
public class WinProcessSteps extends AbstractSteps {

    @Autowired
    private ICommandPrompt commandPrompt;

    @TestStep("получить PID по порту")
    public long getPidByPort(int port){
        switch (OsUtils.getOperatingSystemType()) {
            case Windows:
            case WindowsServer:
                String out = commandPrompt.runCommand(String.format("netstat -ano | findstr %s", port));
                Pattern pattern = Pattern.compile("LISTENING\\s+(\\d+)");
                Matcher matcher = pattern.matcher(out);
                if (matcher.find()){
                    return Long.parseLong(matcher.group(1));
                }
                break;
            default:
                fail(message("wrongOsTypeAssertMessage"));
                break;
        }
        return 0;
    }

    @TestStep("получить PID родительского процесса")
    public long getParentPid(long pid, String name){
        switch (OsUtils.getOperatingSystemType()) {
            case Windows:
            case WindowsServer:
                String out = commandPrompt.runCommand(String.format("wmic process get processid,parentprocessid,executablepath|find \"%s\"", pid));
                Pattern pattern = Pattern.compile(String.format("%s\\..*\\s+(\\d+)", name));
                Matcher matcher = pattern.matcher(out);
                if (matcher.find()){
                    return Long.parseLong(matcher.group(1));
                }
                break;
            default:
                fail(message("wrongOsTypeAssertMessage"));
                break;
        }
        return 0;
    }

    @TestStep("показать окно")
    public void showWindow(long pid){
        Windows32Utils.showWindow(pid);
    }

}
