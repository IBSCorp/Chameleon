package ru.ibsqa.qualit.context;

public interface ISearchElementException {

    default void throwSearchElementException(String message) throws SearchElementException {
        throw new SearchElementException(message);
    }
}
