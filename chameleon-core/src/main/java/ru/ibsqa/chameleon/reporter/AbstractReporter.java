package ru.ibsqa.chameleon.reporter;

public abstract class AbstractReporter implements IReporter {
    public void createAttachment(String message, byte[] bytes, String mimeType, String extension, boolean isStepAttachment) {

    }
    public void writeEnvironment(Environment environment) {

    }
}
