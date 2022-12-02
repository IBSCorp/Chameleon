package ru.ibsqa.qualit.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Это пустая имплементация, она ничего не делает, только выводит обращения в лог
 * Реальные имплементации должны быть описаны в xml конфигурации с параметром primary="true"
 */
@Component
@Slf4j
public class TasktrackerAdapterStubImpl implements ITasktrackerAdapter {

    @Override
    public String getSuite() {
        log.info("call getSuite");
        return null;
    }

    @Override
    public void changeTestCase(TaskStatus status, List<String> attachments, String comment) {
        log.info(String.format("call changeTestCase(status=%s, attachments=%s, comment=%s)", status, attachments, comment));
    }

}
