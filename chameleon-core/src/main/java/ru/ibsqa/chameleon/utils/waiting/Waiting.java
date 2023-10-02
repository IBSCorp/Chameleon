package ru.ibsqa.chameleon.utils.waiting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import ru.ibsqa.chameleon.elements.IFacade;
import ru.ibsqa.chameleon.elements.IFacadeWait;
import ru.ibsqa.chameleon.elements.InvokeFieldException;
import ru.ibsqa.chameleon.utils.spring.SpringUtils;

import java.time.Duration;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Slf4j
public class Waiting {

    private final WaitingUtils waitingUtils = SpringUtils.getBean(WaitingUtils.class);
    private final Set<Class<? extends Throwable>> ignoredExceptions = new HashSet<>();
    private final Duration timeout;

    private Waiting(@NonNull final Duration timeout) {
        this.timeout = timeout;
    }

    private Waiting(@NonNull final IFacade element) {
        if (element instanceof IFacadeWait) {
            this.timeout = Duration.ofSeconds(((IFacadeWait) element).getWaitTimeOut());
            ignoredExceptions.add(InvokeFieldException.class);
        } else {
            this.timeout = Duration.ZERO;
        }
    }

    /**
     * Ожидание настраивается на основе параметров элемента. Таймаут берется из
     * {@link IFacadeWait#getWaitTimeOut()} элемента.
     * Исключения {@link InvokeFieldException} в этом варианте ожидания автоматически игнорируются.
     * Если элемент не реализует интерфейс {@link IFacadeWait}, то дальнейшие действия над элементом
     * выполняются только единожды и исключения не игнорируются.
     *
     * @param element элемент
     * @return {@link Waiting}
     */
    public static Waiting on(@NonNull final IFacade element) {
        return new Waiting(element);
    }

    /**
     * Ожидание настраивается в рамках таймаута
     *
     * @param timeout размер таймаута
     * @return {@link Waiting}
     */
    public static Waiting on(@NonNull final Duration timeout) {
        return new Waiting(timeout);
    }

    /**
     * Добавить исключение в список игнорируемых, пока не закончен таймаут
     *
     * @param ignoredException класс исключения
     * @return {@link Waiting}
     */
    public Waiting ignoring(@NonNull final Class<? extends Throwable> ignoredException) {
        ignoredExceptions.add(ignoredException);
        return this;
    }

    /**
     * Выполнять действия в рамках ожидания.
     * При выполнении этих итерационных действий вывод событий в отчетные системы заблокирован,
     * а заданные исключения игнорируются. Попытки производятся, пока не будет закончен таймаут.
     *
     * @param supplier лямбда-выражение, возвращающее {@link ExploreResult}
     * @param <T> тип данных, которые запоминаются при выполнении действия и затем могут быть проанализированы
     * @return последний результат {@link ExploreResult},
     * либо генерируется исключение, если оно не пропало в рамках таймаута
     */
    public <T> ExploreResult<T> explore(@NonNull final Supplier<ExploreResult<T>> supplier) {
        return waitingUtils.explore(timeout, supplier, ignoredExceptions);
    }

    /**
     * Выполнять проверку в рамках ожидания.
     * При выполнении этой итерационной проверки вывод событий в отчетные системы заблокирован,
     * а заданные исключения игнорируются. Проверки производятся, пока не будет закончен таймаут,
     * либо проверка не вернет true.
     *
     * @param supplier лямбда-выражение, возвращающее boolean
     * @return true, если проверка завершилась успешно,
     * либо генерируется исключение, если оно не пропало в рамках таймаута
     */
    public boolean isPositive(@NonNull final BooleanSupplier supplier) {
        return explore(() -> new ExploreResult<Void>(supplier.getAsBoolean()))
                .isPositive();
    }

    /**
     * Выполнять проверку в рамках ожидания.
     * При выполнении этой итерационной проверки вывод событий в отчетные системы заблокирован,
     * а заданные исключения игнорируются. Проверки производятся, пока не будет закончен таймаут,
     * либо проверка не вернет true.
     *
     * @param supplier лямбда-выражение, возвращающее boolean
     * @return true, если проверка завершилась неуспешно,
     * либо генерируется исключение, если оно не пропало в рамках таймаута
     */
    public boolean isNegative(@NonNull final BooleanSupplier supplier) {
        return !isPositive(supplier);
    }

    /**
     * Выполнять проверку в рамках ожидания.
     * При выполнении этой итерационной проверки вывод событий в отчетные системы заблокирован,
     * а заданные исключения игнорируются. Проверки производятся, пока не будет закончен таймаут,
     * либо проверка не вернет true.
     *
     * @param supplier лямбда-выражение, возвращающее boolean
     * @return последний результат {@link CheckResult},
     * либо генерируется исключение, если оно не пропало в рамках таймаута
     */
    public CheckResult check(@NonNull final BooleanSupplier supplier) {
        return explore(() -> new ExploreResult<Void>(supplier.getAsBoolean()));
    }

    /**
     * Выполнять действия в рамках ожидания.
     * При выполнении этого действия, вывод событий в отчетные системы заблокирован,
     * а заданные исключения игнорируются. Если при выполнении действия выброшено игнорируемое исключение, то
     * попытки выполнить действие будут повторяться, пока не будет закончен таймаут.
     * Генерируется исключение, если оно не пропало в рамках таймаута.
     *
     * @param applier лямбда-выражение, выполняющее действие
     *
     */
    public void apply(@NonNull final Applier applier) {
        explore(() -> {
            applier.apply();
            return new ExploreResult<Void>(true);
        });
    }

    /**
     * Получить значение в рамках ожидания.
     * При выполнении этого действия, вывод событий в отчетные системы заблокирован,
     * а заданные исключения игнорируются. Если при получении данных выброшено игнорируемое исключение, то
     * попытки получить данные будут повторяться, пока не будет закончен таймаут.
     *
     * @param supplier лямбда-выражение, возвращающее данные
     * @param <T> тип данных, которые извлекаются и затем могут быть проанализированы
     * @return результат,
     * либо генерируется исключение, если оно не пропало в рамках таймаута
     */
    public <T> T get(@NonNull final Supplier<T> supplier) {
        return explore(() -> {
                    T data = supplier.get();
                    return ExploreResult.create(true, data);
                })
                .getData();
    }

}
