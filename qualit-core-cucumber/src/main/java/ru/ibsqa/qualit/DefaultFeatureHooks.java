package ru.ibsqa.qualit;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.java.After;
import io.cucumber.messages.types.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import ru.ibsqa.qualit.asserts.AssertLayer;
import ru.ibsqa.qualit.asserts.IAssertManager;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.steps.HiddenStep;
import ru.ibsqa.qualit.storage.IVariableStorage;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class DefaultFeatureHooks {

    @Before(order = 1)
    @HiddenStep
    public void loadVariables(Scenario scenario) {
        IAssertManager assertManager = SpringUtils.getBean(IAssertManager.class);
        assertManager.openLayer();

        IVariableStorage variableStorage = SpringUtils.getBean(IVariableStorage.class);
        IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

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

    @After(order = 1)
    @HiddenStep
    public void checkSoftAssertions(Scenario scenario) {
        IAssertManager assertManager = SpringUtils.getBean(IAssertManager.class);
        AssertLayer assertLayer = assertManager.closeLayer();
        // Если в процессе выполнения были обработаны и собранны SoftAssert-ошибки,
        // то сгенерировать исключение на основе первого из них
        if (assertLayer.hasErrors()) {
            Throwable throwable = assertLayer.getErrors().get(0);
            Assertions.fail(throwable.getMessage(), throwable);
        }
    }

}