package ru.ibsqa.chameleon.uia.launcher;

import ru.ibsqa.chameleon.utils.commandpromt.ICommandPrompt;
import ru.ibsqa.chameleon.utils.waiting.WaitingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultAppLauncher implements IAppLauncher {

    private static final int PROCESS_WAIT_TIMEOUT = 10;
    private static final String PROCESS_WAIT_COMMAND = "tasklist /FI \"IMAGENAME eq %s\"";

    @Autowired
    private WaitingUtils waitingUtils;

    @Autowired
    private ICommandPrompt commandPrompt;

    @Override
    public void start() {
        start(System.getProperty("app.exec.path"));
        waitAppProcess(System.getProperty("app.process.name"));
    }

    protected int getProcessWaitTimeout() {
        return PROCESS_WAIT_TIMEOUT;
    }

    protected String getProcessWaitCommand() {
        return PROCESS_WAIT_COMMAND;
    }

    protected void start(String appExecPath) {
        try {
            Runtime.getRuntime().exec(appExecPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ожидание процесса
     *
     * @param appProcessName
     * @return
     */
    protected boolean waitAppProcess(String appProcessName) {
        return waitingUtils.waiting(getProcessWaitTimeout() * 1000, () ->
                isProcessExists(appProcessName)
        );
    }

    /**
     * Проверка зарегистрирован ли процесс в Task Manager
     *
     * @param process
     * @return
     */
    protected boolean isProcessExists(String process) {
        return commandPrompt.runCommand(String.format(getProcessWaitCommand(), process)).contains("PID");
    }

}
