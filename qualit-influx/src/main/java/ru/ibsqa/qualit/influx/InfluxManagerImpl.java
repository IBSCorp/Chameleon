package ru.ibsqa.qualit.influx;

import ru.ibsqa.qualit.influx.config.InfluxConfig;
import ru.ibsqa.qualit.influx.context.TestContext;
import ru.ibsqa.qualit.influx.providers.IFieldProvider;
import ru.ibsqa.qualit.influx.providers.ITagProvider;
import ru.ibsqa.qualit.influx.providers.Moment;
import ru.ibsqa.qualit.influx.tools.IInfluxTools;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Stack;

/**
 * Стандартная имплементация основного движка, который фиксирует в Influx события начала и окончания сценария
 */
@Slf4j
@Component
public class InfluxManagerImpl implements IInfluxManager {

    @Autowired
    private InfluxConfig influxConfig;

    @Autowired
    private IInfluxTools influxTools;

    private TestContext testContext;
    private Stack<TestContext> stepStack = new Stack<>();
    private Stack<TestContext> endStepStack = new Stack<>();

    @Getter
    private InfluxDB influxDB;

    @PostConstruct
    private void init() {
        if (!influxConfig.isEnabled()) {
            return;
        }

        this.influxDB = influxTools.instanceOf(influxConfig.getUrl(), influxConfig.getUsername(), influxConfig.getPassword());

        if (!influxTools.isDataBaseExists(influxDB, influxConfig.getDatabase())) {
            influxTools.createDatabase(influxDB, influxConfig.getDatabase());
        }
    }

    // Список обработчиков, предоставляющих данные для полей
    private List<IFieldProvider> fieldProviderList;

    // Соберем все подходящие имплементации автоматически
    @Autowired
    private void collectFieldProviders(List<IFieldProvider> fieldProviderList) {
        this.fieldProviderList = fieldProviderList;
    }

    // Список обработчиков, предоставляющих данные для тегов
    private List<ITagProvider> tagProviderList;

    // Соберем все подходящие имплементации автоматически
    @Autowired
    private void collectTagProviders(List<ITagProvider> tagProviderList) {
        this.tagProviderList = tagProviderList;
    }

    public void increaseErrorCounter() {
        testContext.setErrCount(testContext.getErrCount() + 1);
    }

    @Override
    public void startTest(String name) {
        if (!influxConfig.isEnabled()) {
            return;
        }

        this.testContext = new TestContext(name);
        testContext.getInterval().start();

        // добавление полей и тегов перед началом теста
        fieldProviderList.forEach(provider ->
                testContext.getInfluxEntry().addField(provider.getFieldData(influxConfig, Moment.BEFORE_TEST, testContext)));

        tagProviderList.forEach(provider ->
                testContext.getInfluxEntry().addTag(provider.getTagData(influxConfig, Moment.BEFORE_TEST, testContext)));
    }


    @Override
    public void endTest(String status) {
        if (!influxConfig.isEnabled()) {
            return;
        }

        commitStep();

        testContext.getInterval().end();
        testContext.setStatus(status);

        // добавление полей и тегов после выполнения теста
        fieldProviderList.forEach(provider ->
                testContext.getInfluxEntry().addField(provider.getFieldData(influxConfig, Moment.AFTER_TEST, testContext)));

        tagProviderList.forEach(provider ->
                testContext.getInfluxEntry().addTag(provider.getTagData(influxConfig, Moment.AFTER_TEST, testContext)));

        Point point = influxTools.createPoint(influxConfig.getMeasurement(), testContext.getInfluxEntry());
        influxTools.writePoint(getInfluxDB(), influxConfig.getDatabase(), point, influxConfig.getRetentionPolicy());
    }

    @Override
    public void startStep(String name) {
        if (!influxConfig.isEnabled()) {
            return;
        }

        commitStep();

        TestContext stepContext = new TestContext(name);
        stepStack.push(stepContext);
        stepContext.getInterval().start();

        // добавление полей и тегов перед выполнением шага
        fieldProviderList.forEach(provider ->
                stepContext.getInfluxEntry().addField(provider.getFieldData(influxConfig, Moment.BEFORE_STEP, stepContext)));

        tagProviderList.forEach(provider ->
                stepContext.getInfluxEntry().addTag(provider.getTagData(influxConfig, Moment.BEFORE_STEP, stepContext)));
    }

    @Override
    public void endStep(String status) {
        if (!influxConfig.isEnabled()) {
            return;
        }

        TestContext stepContext = stepStack.pop();
        stepContext.getInterval().end();
        stepContext.setStatus(status);
        endStepStack.push(stepContext);
    }

    @Override
    public void endStep(String status, String failureStep, String failureMessage) {
        if (!influxConfig.isEnabled()) {
            return;
        }

        if (failureStep != null) {
            testContext.setFailureStep(failureStep);
        }
        if (failureMessage != null) {
            testContext.setFailureMessage(failureMessage);
        }

        endStepStack.peek().setStatus(status);
        commitStep();
    }

    private void commitStep() {

        if (endStepStack.size() > 0) {

            TestContext stepContext = endStepStack.pop();

            // добавление полей и тегов после выполнения шага
            fieldProviderList.forEach(provider ->
                    stepContext.getInfluxEntry().addField(provider.getFieldData(influxConfig, Moment.AFTER_STEP, stepContext)));

            tagProviderList.forEach(provider ->
                    stepContext.getInfluxEntry().addTag(provider.getTagData(influxConfig, Moment.AFTER_STEP, stepContext)));

            Point point = influxTools.createPoint(influxConfig.getMeasurement(), stepContext.getInfluxEntry());
            influxTools.writePoint(getInfluxDB(), influxConfig.getDatabase(), point, influxConfig.getRetentionPolicy());

        }
    }

}