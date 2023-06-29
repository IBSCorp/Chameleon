package ru.ibsqa.chameleon.context;

public interface ISearchElementException {

    default void throwSearchElementException(String message) throws SearchElementException {
        throw new SearchElementException(message);
    }
}
