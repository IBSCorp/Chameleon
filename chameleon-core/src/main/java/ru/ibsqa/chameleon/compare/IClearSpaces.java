package ru.ibsqa.chameleon.compare;

import java.util.Objects;

public interface IClearSpaces {
    default String clearSpaces(String value) {
        return Objects.isNull(value) ? null : value.trim().replaceAll("[\\s\\p{Z}]+", "");
    }
}
