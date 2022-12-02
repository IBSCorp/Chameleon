package ru.ibsqa.qualit.steps;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.qualit.steps.roles.Value;

import java.io.File;

public class AttachmentStorySteps extends AbstractSteps {

    @Autowired
    private AttachmentSteps attachmentSteps;

    @Когда("^добавить к результату теста файл \"([^\"]*)\"$")
    public void createAttachment(@Value String filePath) {
        flow(() ->
                attachmentSteps.createAttachment(new File(filePath))
        );
    }

    @Когда("^добавить к результату теста файл \"([^\"]*)\" с типом \"([^\"]*)\"$")
    public void createAttachment(@Value String filePath, @Value String mimeType) {
        flow(() ->
                attachmentSteps.createAttachment(new File(filePath), mimeType)
        );
    }

}
