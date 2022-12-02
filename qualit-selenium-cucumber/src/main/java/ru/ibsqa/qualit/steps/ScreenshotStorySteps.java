package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.steps.roles.Value;
import io.cucumber.java.ru.Тогда;
import org.springframework.beans.factory.annotation.Autowired;

public class ScreenshotStorySteps extends AbstractSteps {

    @Autowired
    private IScreenshotManager screenshotManager;


    @StepDescription(action = "UI->Прочее->Сделать скриншот", parameters = {"name - наименование скриншота"})
    @Тогда("^сохранить скриншот \"([^\"]*)\"")
    public void takeScreenshotToReport(
            @Value String name
    ) {
        flow(()->
                screenshotManager.takeScreenshotToReport(name)
        );
    }
}
