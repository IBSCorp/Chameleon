package ru.ibsqa.chameleon.reporter;

import org.jbehave.core.model.Story;

public interface IScenarioManager {
    String getScenarioName(Story currentStory, Story parentStory, String scenarioTitle);
}
