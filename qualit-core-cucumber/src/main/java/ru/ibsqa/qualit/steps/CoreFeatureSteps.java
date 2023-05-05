package ru.ibsqa.qualit.steps;

import io.cucumber.core.backend.Glue;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.backend.TestCaseState;
import io.cucumber.core.feature.FeatureParser;
import io.cucumber.core.feature.FeatureWithLines;
import io.cucumber.core.gherkin.Feature;
import io.cucumber.core.gherkin.Pickle;
import io.cucumber.core.gherkin.Step;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.core.runner.Runner;
import io.cucumber.core.runtime.CucumberExecutionContext;
import io.cucumber.core.runtime.FeaturePathFeatureSupplier;
import io.cucumber.core.runtime.FeatureSupplier;
import io.cucumber.core.runtime.Runtime;
import io.cucumber.core.stepexpression.StepTypeRegistry;
import io.cucumber.messages.types.Comment;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.GherkinDocument;
import io.cucumber.spring.QualITFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.storage.IVariableScope;
import ru.ibsqa.qualit.storage.IVariableStorage;
import ru.ibsqa.qualit.storage.VariableScopeImpl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Component
@Slf4j
public class CoreFeatureSteps extends AbstractSteps {
    private List<IFeatureSteps> featureStepsImpls;

    @Autowired
    private void collectImplementations(List<IFeatureSteps> featureSteps) {
        this.featureStepsImpls = featureSteps;
    }

    public void executeScenario(String scenario, String function) {
        executeScenario(scenario, function, new HashMap<>());
    }

    @TestStep("выполняется сценарий \"${scenario}\" из функционала \"${function}\" с параметрами: \"${params}\"")
    public Map<String, String> executeScenario(String scenario, String function, Map<String, String> params) {
        IVariableStorage variableStorage = getVariableStorage();
        IVariableScope globalScope = variableStorage.getDefaultScope();
        IVariableScope localScope = new VariableScopeImpl();

        // Сохраняем в локальный variableStorage переданные параметры
        variableStorage.setDefaultScope(localScope);
        for (Map.Entry<String, String> paramsEntry : params.entrySet()) {
            variableStorage.setVariable(paramsEntry.getKey(), paramsEntry.getValue());
        }

        // Получаем все необходимые объекты cucumber
        try {
            RuntimeOptions runtimeOptions = getRunTimeOptions();
            FeatureSupplier featureSupplier = getFeatureSupplier(runtimeOptions);
            Feature feature = getFeature(scenario, function, featureSupplier);
            Pickle pickle = getPickle(scenario, function, feature);
            Glue glue = getGlue(pickle, runtimeOptions, featureSupplier);

            // Сохраняем в локальный variableStorage переменные вызываемого теста и проверяем, что переданы все необходимые параметры для теста
            loadVariables(feature, variableStorage, params);

            // Запускаем шаги вызываемого теста
            for (Step step : pickle.getSteps()) {
                try {
                    featureStepsImpls.forEach(impl -> impl.beforeStep(step));
                    runStep(step, feature, glue);
                    featureStepsImpls.forEach(impl -> impl.afterStep(step));
                } catch (Exception exception) {
                    featureStepsImpls.forEach(impl -> impl.afterException(step));
                    fail(exception.getLocalizedMessage());
                } finally {
                    featureStepsImpls.forEach(impl -> impl.finallyStep(step));
                }
            }

            // Перезаписываем значения переданных параметров, если они изменились в ходе работы теста
            for (Map.Entry<String, Object> storage : variableStorage.getVariables().entrySet()) {
                if (params.containsKey(storage.getKey())) {
                    params.put(storage.getKey(), storage.getValue().toString());
                }
            }
        } finally {
            // Сохраняем в глобальный variableStorage переданные параметры с новыми значениями (или со старыми, если они не поменялись в ходе работы теста)
            variableStorage.setDefaultScope(globalScope);
            for (Map.Entry<String, String> paramsEntry : params.entrySet()) {
                variableStorage.setVariable(paramsEntry.getKey(), paramsEntry.getValue());
            }
        }

        return params;
    }

    private void loadVariables(Feature feature, IVariableStorage variableStorage, Map<String, String> params) {
        List<Envelope> envelopes = (List<Envelope>) feature.getParseEvents();
        GherkinDocument gherkinDocument = envelopes.stream()
                .map(Envelope::getGherkinDocument)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow();

        Pattern variablePattern = Pattern.compile("\\$(.*?)\\s(.*)");
        Pattern paramPattern = Pattern.compile("&(.*?)\\s(.*)");

        for (Comment comment : gherkinDocument.getComments()) {
            Matcher variableMatcher = variablePattern.matcher(comment.getText());
            if (variableMatcher.find()) {
                variableStorage.setVariable(variableMatcher.group(1), getEvaluateManager().evalVariable(variableMatcher.group(2)));
            }

            Matcher paramMatcher = paramPattern.matcher(comment.getText());
            if (paramMatcher.find()) {
                assertTrue(params.containsKey(paramMatcher.group(1)), "Тест вызван без обязательного параметра <" + paramMatcher.group(1) + ">");
            }
        }
    }

    private List<URI> getCucumberGlueList() {
        List<URI> cucumberGlueList = new ArrayList<>();

        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cucumber.properties")) {
            Properties cucumberProperties = new Properties();
            cucumberProperties.load(stream);
            String cucumberGlue = cucumberProperties.getProperty("cucumber.glue").replaceAll("\\.", "/");

            for (String glue : cucumberGlue.split(",")) {
                cucumberGlueList.add(new URI("classpath:/" + glue));
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return cucumberGlueList;
    }

    private RuntimeOptions getRunTimeOptions() {
        RuntimeOptions runtimeOptions = RuntimeOptions.defaultOptions();

        try {
            Method setFeaturePaths = RuntimeOptions.class.getDeclaredMethod("setFeaturePaths", List.class);
            setFeaturePaths.setAccessible(true);
            setFeaturePaths.invoke(runtimeOptions, Collections.singletonList(FeatureWithLines.parse("src/test/resources")));

            Method setGlue = RuntimeOptions.class.getDeclaredMethod("setGlue", List.class);
            setGlue.setAccessible(true);
            setGlue.invoke(runtimeOptions, getCucumberGlueList());

            Method setObjectFactoryClass = RuntimeOptions.class.getDeclaredMethod("setObjectFactoryClass", Class.class);
            setObjectFactoryClass.setAccessible(true);
            setObjectFactoryClass.invoke(runtimeOptions, QualITFactory.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return runtimeOptions;
    }

    private FeatureSupplier getFeatureSupplier(RuntimeOptions runtimeOptions) {
        return new FeaturePathFeatureSupplier(() -> Thread.currentThread().getContextClassLoader(),
                runtimeOptions,
                new FeatureParser(UUID::randomUUID));
    }

    private Feature getFeature(String scenario, String function, FeatureSupplier featureSupplier) {
        return featureSupplier.get().stream()
                .filter(feature -> feature.getName().isPresent() && feature.getName().get().equals(function))
                .filter(feature -> feature.getPickles().stream().anyMatch(pickle -> pickle.getName().equals(scenario)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Не найден сценарий [%s] из функционала [%s]", scenario, function)));
    }

    private Pickle getPickle(String scenario, String function, Feature feature) {
        return feature.getPickles().stream()
                .filter(pickle -> pickle.getName().equals(scenario))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Не найден сценарий [%s] из функционала [%s]", scenario, function)));
    }

    private Glue getGlue(Pickle pickle, RuntimeOptions runtimeOptions, FeatureSupplier featureSupplier) {
        try {
            Runtime runtime = Runtime.builder()
                    .withRuntimeOptions(runtimeOptions)
                    .withClassLoader(() -> Thread.currentThread().getContextClassLoader())
                    .withFeatureSupplier(featureSupplier)
                    .build();

            Field context = Runtime.class.getDeclaredField("context");
            context.setAccessible(true);
            CucumberExecutionContext cucumberExecutionContext = (CucumberExecutionContext) context.get(runtime);

            Method getRunner = CucumberExecutionContext.class.getDeclaredMethod("getRunner");
            getRunner.setAccessible(true);
            Runner runner = (Runner) getRunner.invoke(cucumberExecutionContext);

            Field fieldGlue = Runner.class.getDeclaredField("glue");
            fieldGlue.setAccessible(true);
            Glue glue = (Glue) fieldGlue.get(runner);

            Method prepareGlue = glue.getClass().getDeclaredMethod("prepareGlue", StepTypeRegistry.class);
            prepareGlue.setAccessible(true);
            prepareGlue.invoke(glue, new StepTypeRegistry(new Locale(pickle.getLanguage())));

            Field objectFactoryField = Runner.class.getDeclaredField("objectFactory");
            objectFactoryField.setAccessible(true);
            ObjectFactory objectFactory = (ObjectFactory) objectFactoryField.get(runner);
            objectFactory.start();

            return glue;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void runStep(Step step, Feature feature, Glue glue) {
        try {
            Method stepDefinitionMatch = glue.getClass().getDeclaredMethod("stepDefinitionMatch", URI.class, Step.class);
            stepDefinitionMatch.setAccessible(true);
            Object pickleStepDefinitionMatch = stepDefinitionMatch.invoke(glue, feature.getUri(), step);

            Method runStep = pickleStepDefinitionMatch.getClass().getDeclaredMethod("runStep", TestCaseState.class);
            runStep.setAccessible(true);
            runStep.invoke(pickleStepDefinitionMatch, new Object[]{null});
        } catch (Throwable exception) {
            Throwable throwable = exception;
            if (exception instanceof InvocationTargetException) {
                throwable = ((InvocationTargetException) exception).getTargetException();
            }
            log.error(throwable.getLocalizedMessage(), throwable);
            throw new RuntimeException(throwable);
        }
    }
}