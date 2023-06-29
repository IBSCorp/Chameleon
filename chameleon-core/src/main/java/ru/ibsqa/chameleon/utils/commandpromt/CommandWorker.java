package ru.ibsqa.chameleon.utils.commandpromt;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class CommandWorker implements Runnable {

    private String command;
    private boolean complete = false;
    private String output = "";

    @Getter
    private Process process;

    public CommandWorker(String command) {
        this.command = command;
    }

    @Override
    public void run() {

        String charsetName = null;
        BufferedReader stdout = null;
        try {
            ProcessBuilder builder;
            builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            process = builder.start();
            charsetName = "CP866";
            stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), charsetName));

            String line;
            while ((line = stdout.readLine()) != null && !Thread.interrupted()) {
                if (!isComplete()) {
                    log.info(line);
                } else {
                    log.trace(line);
                }
                addOutput(line);
                if (line.contains("Starting Windows Desktop Driver on port") || line.contains("jira integration started")
                        || line.contains("webservice jiraintegration started")) {
                    //if (line.contains("Starting Windows Desktop Driver on port")) {
                    setComplete(true);
                }
            }
            if (Thread.interrupted()) {
                process.destroyForcibly();
            }
            setComplete(true);

        } catch (Exception e) {
            fail("Ошибка при выполнении команды " + command + " : " + e);
            setComplete(true);
        }
    }

    public synchronized void setComplete(boolean complete) {
        this.complete = complete;
    }

    public synchronized boolean isComplete() {
        return complete;
    }

    public synchronized String getOutput() {
        return output;
    }

    public synchronized void addOutput(String line) {
        this.output = this.output + "" + line + "\n";
    }

}
