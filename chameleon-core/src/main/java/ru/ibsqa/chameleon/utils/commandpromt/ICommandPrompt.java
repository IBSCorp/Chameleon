package ru.ibsqa.chameleon.utils.commandpromt;

public interface ICommandPrompt {
    String runCommand(String command);
    Thread runCommandThread(String command);
}
