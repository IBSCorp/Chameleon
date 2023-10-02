package ru.ibsqa.chameleon.utils.waiting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.steps.aspect.IStepListenerManager;
import ru.ibsqa.chameleon.utils.error.ExceptionUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Component
@Slf4j
public class WaitingUtils {

    @Autowired
    private IStepListenerManager stepListenerManager;

    /**
     * Выполнять ожидание, пока не будет достигнут результат true или не истечет таймаут
     * (в этом случае возвращается false)
     *
     * @param timeout размер таймаута в мс
     * @param supplier лямбда-выражение, возвращающее результат true или false
     * @return результат
     */
    public boolean waiting(
            final long timeout,
            @NonNull final  BooleanSupplier supplier
    ) {
        long endtime = System.currentTimeMillis() + timeout;
        do {
            if (supplier.getAsBoolean()) {
                return true;
            }
        } while (System.currentTimeMillis() < endtime);
        return false;
    }

    /**
     * Выполнять ожидание, пока не будет достигнут результат true или не истечет таймаут
     * (в этом случае возвращается false)
     *
     * @param timeout размер таймаута {@link Duration}
     * @param supplier лямбда-выражение, возвращающее результат true или false
     * @return результат
     */
    public boolean waiting(
            @NonNull final Duration timeout,
            @NonNull final BooleanSupplier supplier
    ) {
        return waiting(timeout.toMillis(), supplier);
    }

    /**
     * Выполнять действия в рамках ожидания.
     * При выполнении этих итерационных действий вывод событий в отчетные системы заблокирован,
     * а заданные исключения игнорируются. Попытки производятся, пока не будет закончен таймаут.
     *
     * @param supplier лямбда-выражение, возвращающее результат
     * @param ignoredExceptions список игнорируемых исключений
     * @param <T> тип результата
     * @return результат
     */
    public <T extends WaitResult> T explore(
            @NonNull final Duration timeout,
            @NonNull final Supplier<T> supplier,
            @NonNull final Collection<Class<? extends Throwable>> ignoredExceptions
    ) {
        boolean ignoredMode = stepListenerManager.isIgnoredMode();
        stepListenerManager.setIgnoredMode(true);
        try {
            final ThreadLocal<Throwable> lastException = new InheritableThreadLocal<>();
            final ThreadLocal<T> checkResult = new InheritableThreadLocal<>();
            final boolean complete = this.waiting(timeout, () -> {
                lastException.remove();
                try {
                    checkResult.set(supplier.get());
                    return checkResult.get().isPositive();
                } catch (Throwable e) {
                    log.debug(e.getMessage(), e);
                    lastException.set(e);
                    if (!ignoredExceptions.isEmpty() &&
                            ignoredExceptions.stream()
                                    .anyMatch((ignoredExceptionClass) ->
                                            ignoredExceptionClass.isAssignableFrom(e.getClass())
                                    )
                    ) {
                        return false;
                    }
                }
                Optional.ofNullable(lastException.get())
                        .ifPresent(ExceptionUtils::throwUnchecked);
                return false;
            });
            if (!complete) {
                Optional.ofNullable(lastException.get())
                        .ifPresent(ExceptionUtils::throwUnchecked);
            }
            return checkResult.get();
        } finally {
            stepListenerManager.setIgnoredMode(ignoredMode);
        }
    }

}
