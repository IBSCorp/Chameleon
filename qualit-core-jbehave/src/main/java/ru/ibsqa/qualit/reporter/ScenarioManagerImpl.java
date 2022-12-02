package ru.ibsqa.qualit.reporter;

import org.jbehave.core.model.Story;
import org.springframework.stereotype.Component;

@Component
public class ScenarioManagerImpl implements IScenarioManager {
    @Override
    public String getScenarioName(Story currentStory, Story parentStory, String scenarioTitle) {
        return String.format("%s: %s", currentStory.getName(), scenarioTitle);
    }
}
