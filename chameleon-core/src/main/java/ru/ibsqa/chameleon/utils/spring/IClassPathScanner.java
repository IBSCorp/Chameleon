package ru.ibsqa.chameleon.utils.spring;

import java.util.stream.Stream;

/**
 * Сканнер классов
 */
public interface IClassPathScanner {
    <E extends Class<T>, T> Stream<E> findCandidates(Class<T> assignableType);
}
