package ru.ibsqa.chameleon.definitions.repository;

import java.util.Optional;

public interface IXmlRepositoryType {
    default String getNamespace() {
        return Optional.ofNullable(getTypeName())
                .map(n -> Namespace.PREFIX+n)
                .orElse(null);
    }
    String getTypeName();
    Class<? extends IRepositoryElement> getAssignableFrom();
}
