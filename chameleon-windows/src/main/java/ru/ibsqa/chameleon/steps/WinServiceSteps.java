package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.enums.ServiceStatus;
import ru.ibsqa.chameleon.utils.commandpromt.ICommandPrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

@Component
public class WinServiceSteps extends AbstractSteps {

    private final long DEFAULT_TIMEOUT = 120000L;

    @Autowired
    private ICommandPrompt commandPrompt;

    @Autowired
    private CoreUtilSteps utilSteps;

    @TestStep("запущена служба \"${serviceName}\"")
    public void startServiceStep(String serviceName) {
        commandPrompt.runCommand(String.format("sc start %s",serviceName));
        waitForStatus(serviceName, ServiceStatus.RUNNING, DEFAULT_TIMEOUT);
        // Запас на завершение переходных процессов
        utilSteps.stopExecuted(10);
    }

    @TestStep("остановлена служба \"${serviceName}\"")
    public void stopServiceStep(String serviceName) {
        commandPrompt.runCommand(String.format("sc stop %s",serviceName));
        waitForStatus(serviceName, ServiceStatus.STOPPED, DEFAULT_TIMEOUT);
        // Запас на завершение переходных процессов
        utilSteps.stopExecuted(10);
    }

    /**
     *
     * @param serviceName Имя сервиса
     * @param status Ожидаемый статус
     * @param timeout Таймаут в мс
     */
    private void waitForStatus(String serviceName, ServiceStatus status, long timeout) {

        if (null == status) {
            return;
        }

        long statTime = System.currentTimeMillis();

        while (status != getServiceStatus(serviceName)) {
            if (System.currentTimeMillis()>statTime+timeout) {
                fail(message("winServiceChangeStatusErrorMessage",
                        Long.toString(timeout),
                        status.name(),
                        serviceName));
                return;
            }
        }

    }

    /**
     * Получить текущий статус сервиса
     * @param serviceName Имя сервиса
     * @return
     */
    private ServiceStatus getServiceStatus(String serviceName) {
        String result = commandPrompt.runCommand(String.format("sc query %s",serviceName));

        return Arrays
                .asList(ServiceStatus.values())
                .stream()
                .filter(
                    item -> result.matches(String.format("(?s)(.*)%s(\\s*)%s(.*)", Integer.toString(item.getCode()), item.name()))
                )
                .findFirst()
                .orElse(ServiceStatus.UNKNOWS);
    }

}
