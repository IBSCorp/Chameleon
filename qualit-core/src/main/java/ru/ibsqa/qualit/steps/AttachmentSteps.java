package ru.ibsqa.qualit.steps;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.reporter.IReporterManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Component
public class AttachmentSteps extends AbstractSteps {

    @Autowired
    private IReporterManager reporterManager;

    @TestStep("добавить к результату теста файлы \"${files}\"")
    public void createAttachments(@NonNull List<File> files) {
        files.forEach(this::createAttachment);
    }

    public void createAttachment(@NonNull File file) {
        this.createAttachment(file, "application/octet-stream");
    }

    public void createAttachment(@NonNull File file, String mimeType) {
        this.createAttachment(file, mimeType, false);
    }

    @TestStep("добавить к результату теста файл \"${file}\" с типом \"${mimeType}\"")
    public void createAttachment(@NonNull File file, String mimeType, boolean isStepAttachment) {
        byte[] bytes;

        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            bytes = new byte[0];
        }

        reporterManager.createAttachment(FilenameUtils.getBaseName(file.getName()), bytes, mimeType, FilenameUtils.getExtension(file.getName()), isStepAttachment);
    }

    public void createAttachment(String name, @NonNull String content, String mimeType) {
        this.createAttachment(name, content, mimeType, false);
    }

    @TestStep("добавить к результату теста содержимое \"${content}\" с типом \"${mimeType}\" и именем \"${name}\"")
    public void createAttachment(String name, @NonNull String content, String mimeType, boolean isStepAttachment) {
        reporterManager.createAttachment(FilenameUtils.getBaseName(name), content.getBytes(), mimeType, FilenameUtils.getExtension(name), isStepAttachment);
    }

}
