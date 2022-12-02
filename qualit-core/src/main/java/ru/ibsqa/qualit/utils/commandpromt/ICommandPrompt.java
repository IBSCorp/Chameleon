package ru.ibsqa.qualit.utils.commandpromt;

public interface ICommandPrompt {
    String runCommand(String command);
    Thread runCommandThread(String command);
}
