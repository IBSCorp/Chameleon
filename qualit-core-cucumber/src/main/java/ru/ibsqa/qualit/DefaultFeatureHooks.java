package ru.ibsqa.qualit;

import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.storage.IVariableStorage;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import io.cucumber.gherkin.Gherkin;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.messages.IdGenerator;
import io.cucumber.messages.types.Comment;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.GherkinDocument;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

@Slf4j
public class DefaultFeatureHooks {

    @Before(order = 1)
    public void loadVariables(Scenario scenario) {
        IVariableStorage variableStorage = SpringUtils.getBean(IVariableStorage.class);
        IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

        try {
            String path;
            if (scenario.getUri().getPath() != null) {
                path = scenario.getUri().getPath();
            } else {
                path = scenario.getUri().toString().replace("classpath:", "src/test/resources/");
            }

            Stream<Envelope> envelopeStream = Gherkin.fromPaths(singletonList(path), false,
                    true, true, new IdGenerator.Incrementing());

            GherkinDocument gherkinDocument = envelopeStream
                    .map(Envelope::getGherkinDocument)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow();

            for (Comment comment : gherkinDocument.getComments()) {
                Pattern p = Pattern.compile("\\$(.*?)\\s(.*)");
                Matcher m = p.matcher(comment.getText());
                if (m.find()) {
                    variableStorage.setVariable(m.group(1), evaluateManager.evalVariable(m.group(2)));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}