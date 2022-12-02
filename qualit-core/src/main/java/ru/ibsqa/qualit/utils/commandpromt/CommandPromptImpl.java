package ru.ibsqa.qualit.utils.commandpromt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        try {
            while (!commandWorker.isComplete()) {
                Thread.sleep(100);
            }
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
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
