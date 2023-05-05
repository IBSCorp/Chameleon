package ru.ibsqa.qualit.utils.commandpromt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.utils.delay.DelayUtils;

@Slf4j
@Component
public class CommandPromptImpl implements ICommandPrompt {

    private CommandWorker commandWorker;

    public String runCommand(String command){
        log.info(command);
        Thread thread;
        commandWorker = new CommandWorker(command);
        thread = new Thread(commandWorker);
        thread.start();
        while (!commandWorker.isComplete()) {
            DelayUtils.sleep(100);
        }
        DelayUtils.sleep(2000);
        return commandWorker.getOutput();
    }

    public Thread runCommandThread(String command){
        log.info(command);
        Thread thread;
        commandWorker = new CommandWorker(command);
        thread = new Thread(commandWorker);
        thread.start();
        return thread;
    }

}
