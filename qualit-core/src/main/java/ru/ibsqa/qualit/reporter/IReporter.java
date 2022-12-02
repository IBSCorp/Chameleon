package ru.ibsqa.qualit.reporter;

public interface IReporter {
    void createAttachment(String message, byte[] bytes, String mimeType, String extension, boolean isStepAttachment);
    void writeEnvironment(Environment environment);
}
