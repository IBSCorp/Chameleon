package ru.ibsqa.chameleon.utils.waiting;

import org.springframework.lang.NonNull;

import java.util.function.Consumer;

/**
 * Класс, содержащий результаты действия при выполнении ожидания
 * @param <T> тип данных, которые запоминается при выполнении ожидания и затем могут быть проанализированы
 */
public class ExploreResult<T> extends CheckResult {

    private final T data;

    ExploreResult(final boolean positive) {
        this(positive, null);
    }

    ExploreResult(final boolean positive, final T data) {
        super(positive);
        this.data = data;
    }

    /**
     * @param data данные
     * @param <T> тип данных
     * @return позитивный результат с заданными данными
     */
    public static <T> ExploreResult<T> positive(final T data) {
        return new ExploreResult<T>(true, data);
    }

    /**
     * @param positive позитивный или негативный результат
     * @param data данные
     * @param <T> тип данных
     * @return результат с заданными данными
     */
    public static <T> ExploreResult<T> create(final boolean positive, final T data) {
        return new ExploreResult<T>(positive, data);
    }

    /**
     * @return данные, полученные при выполнении ожидания
     */
    public T getData() {
        return data;
    }

    /**
     * Выполнить действие, если результат позитивный
     * @param consumer лямбда-выражение, выполняющее действие, принимает полученные данные
     * @return полученные данные
     */
    public T ifPositive(@NonNull final Consumer<T> consumer) {
        if (isPositive()) {
            consumer.accept(data);
        }
        return data;
    }

    /**
     * Выполнить действие, если результат негативный
     * @param consumer лямбда-выражение, выполняющее действие, принимает полученные данные
     * @return полученные данные
     */
    public T ifNegative(@NonNull final Consumer<T> consumer) {
        if (!isPositive()) {
            consumer.accept(data);
        }
        return data;
    }

}
