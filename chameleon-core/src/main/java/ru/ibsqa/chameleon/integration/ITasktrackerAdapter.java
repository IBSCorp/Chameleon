package ru.ibsqa.chameleon.integration;

import java.util.List;

public interface ITasktrackerAdapter {

    String getSuite();

    void changeTestCase(TaskStatus status, List<String> attachments, String comment);

    default void changeTestCase(TaskStatus status, List<String> attachments) {
        changeTestCase(status, attachments, "");
    }

    default void changeTestCase(TaskStatus status) {
        changeTestCase(status, null, "");
    }

}
