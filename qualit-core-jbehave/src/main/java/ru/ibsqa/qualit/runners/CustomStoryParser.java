package ru.ibsqa.qualit.runners;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.storage.IVariableStorage;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Story;
import org.jbehave.core.parsers.RegexStoryParser;

@Slf4j
public class CustomStoryParser extends RegexStoryParser {

    private IVariableStorage variableStorage = SpringUtils.getBean(IVariableStorage.class);
    private IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

    public CustomStoryParser(ExamplesTableFactory examplesTableFactory) {
        super(examplesTableFactory);
    }

    @Override
    public Story parseStory(String storyAsText, String storyPath) {
        log.info("running story >>>> " + storyPath);
        variableStorage.clearVariables();

        final Story story = super.parseStory(storyAsText, storyPath);
        final Meta meta = story.getMeta();
        for (String metaProp : meta.getPropertyNames()) {
            variableStorage.setVariable(metaProp, evaluateManager.evalVariable(meta.getProperty(metaProp)));
        }
        return story;
    }

}

