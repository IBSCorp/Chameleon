package ru.ibsqa.qualit.definitions.repository;

public interface ILoader {
    @Deprecated
    boolean load(IRepositoryWrapper repositoryWrapper, String fileName);
}
