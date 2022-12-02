package ru.ibsqa.qualit.steps;

import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScreenshotStorySteps extends AbstractSteps {

    @Autowired
    private IScreenshotManager screenshotManager;

    @When("сохранить скриншот \"$name\"")
    public void takeScreenshotToReport(String name) {
        flow(()->
                screenshotManager.takeScreenshotToReport(name)
        );
    }
}
