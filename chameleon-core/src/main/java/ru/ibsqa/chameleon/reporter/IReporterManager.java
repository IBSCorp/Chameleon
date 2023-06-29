package ru.ibsqa.chameleon.reporter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public interface IReporterManager {
    void createAttachment(String message, byte[] bytes, String mimeType, String extension, boolean isStepAttachment);

    default void createAttachment(String message, InputStream inputStream, String mimeType, String extension, boolean isStepAttachment) throws IOException {
        createAttachment(message, inputStream.readAllBytes(), mimeType, extension, isStepAttachment);
    }

    default void createAttachment(String message, String string, String mimeType, String extension, boolean isStepAttachment) {
        createAttachment(message, string.getBytes(StandardCharsets.UTF_8), mimeType, extension, isStepAttachment);
    }

    default void createAttachment(String message, byte[] bytes, String mimeType, String extension) {
        createAttachment(message, bytes, mimeType, extension, true);
    }

    default void createAttachment(String message, InputStream inputStream, String mimeType, String extension) throws IOException {
        createAttachment(message, inputStream.readAllBytes(), mimeType, extension, true);
    }

    default void createAttachment(String message, String string, String mimeType, String extension) {
        createAttachment(message, string.getBytes(StandardCharsets.UTF_8), mimeType, extension, true);
    }

    void writeEnvironment(Environment environment);
}
