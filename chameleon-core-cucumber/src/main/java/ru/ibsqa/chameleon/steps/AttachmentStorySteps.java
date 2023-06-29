package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ibsqa.chameleon.steps.roles.Value;

import java.io.File;

public class AttachmentStorySteps extends AbstractSteps {

    @Autowired
    private AttachmentSteps attachmentSteps;

    @StepDescription(action = "Сценарии->Добавить к сценарию файл"
            , subAction = "Добавить к сценарию файл"
            , parameters = {"filePath - путь к файлу"})
    @Когда("^добавить к результату теста файл \"([^\"]*)\"$")
    public void createAttachment(@Value String filePath) {
        flow(() ->
                attachmentSteps.createAttachment(new File(filePath))
        );
    }

    @StepDescription(action = "Сценарии->Добавить к сценарию файл"
            , subAction = "Добавить к сценарию файл с типом"
            , parameters = {"filePath - путь к файлу", "mimeType - тип файла"})
    @Когда("^добавить к результату теста файл \"([^\"]*)\" с типом \"([^\"]*)\"$")
    public void createAttachment(@Value String filePath, @Value String mimeType) {
        flow(() ->
                attachmentSteps.createAttachment(new File(filePath), mimeType)
        );
    }

}
