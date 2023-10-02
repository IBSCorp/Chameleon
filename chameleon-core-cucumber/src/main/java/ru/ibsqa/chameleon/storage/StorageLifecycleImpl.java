package ru.ibsqa.chameleon.storage;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.java.Scenario;
import io.cucumber.messages.types.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.evaluate.IEvaluateManager;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
@Slf4j
public class StorageLifecycleImpl implements IStorageLifecycle {

    @Autowired
    private IVariableStorage variableStorage;

    @Autowired
    private IEvaluateManager evaluateManager;

    @Override
    public void beforeScenario(Scenario scenario) {
        try {
            String path;
            if (scenario.getUri().getPath() != null) {
                path = scenario.getUri().getPath();
            } else {
                path = scenario.getUri().toString().replace("classpath:", "src/test/resources/");
            }

            GherkinParser parser = GherkinParser.builder().build();

            try(FileInputStream inputStream = new FileInputStream(path)) {
                String feature = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                Stream<Envelope> envelopeStream = parser.parse(
                        Envelope.of(new Source(path, feature, SourceMediaType.TEXT_X_CUCUMBER_GHERKIN_PLAIN)));

                GherkinDocument gherkinDocument = envelopeStream
                        .map(Envelope::getGherkinDocument)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst()
                        .orElseThrow();

                for (Comment comment : gherkinDocument.getComments()) {
                    Pattern p = Pattern.compile("\\$(.*?)\\s(.*)");
                    Matcher m = p.matcher(comment.getText());
                    if (m.find()) {
                        variableStorage.setVariable(m.group(1), evaluateManager.evalVariable(m.group(2)));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterScenario(Scenario scenario) {

    }
}
