package ru.ibsqa.chameleon.definitions.repository;

public interface ILoader {
    @Deprecated
    boolean load(IRepositoryWrapper repositoryWrapper, String fileName);
}
