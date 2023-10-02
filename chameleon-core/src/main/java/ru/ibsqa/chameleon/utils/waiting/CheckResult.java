package ru.ibsqa.chameleon.utils.waiting;

import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Класс, содержащий результаты проверки при выполнении ожидания
 */
public class CheckResult implements WaitResult {

    private final boolean positive;

    private static ExploreResult<?> positiveObj = null;
    private static ExploreResult<?> negativeObj = null;

    CheckResult(final boolean positive) {
        this.positive = positive;
    }

    /**
     * @return позитивный результат
     */
    @SuppressWarnings("unchecked")
    public static <T> ExploreResult<T> negative() {
        if (Objects.isNull(negativeObj)) {
            negativeObj = new ExploreResult<>(false);
        }
        return (ExploreResult<T>) negativeObj;
    }

    /**
     * @return негативный результат
     */
    @SuppressWarnings("unchecked")
    public static <T> ExploreResult<T> positive() {
        if (Objects.isNull(positiveObj)) {
            positiveObj = new ExploreResult<>(true);
        }
        return (ExploreResult<T>) positiveObj;
    }

    /**
     * @param positive позитивный или негативный результат
     * @return результат
     */
    public static <T> ExploreResult<T> create(final boolean positive) {
        return positive ? positive() : negative();
    }

    /**
     * @return true, если результат позитивный
     */
    @Override
    public boolean isPositive() {
        return this.positive;
    }

    /**
     * @return true, если результат негативный
     */
    public boolean isNegative() {
        return !isPositive();
    }

    /**
     * Выполнить действие, если результат позитивный
     * @param applier лямбда-выражение, выполняющее действие
     */
    public void ifPositive(@NonNull final Applier applier) {
        if (positive) {
            applier.apply();
        }
    }

    /**
     * Выполнить действие, если результат негативный
     * @param applier лямбда-выражение, выполняющее действие
     */
    public void ifNegative(@NonNull final Applier applier) {
        if (!positive) {
            applier.apply();
        }
    }

}
