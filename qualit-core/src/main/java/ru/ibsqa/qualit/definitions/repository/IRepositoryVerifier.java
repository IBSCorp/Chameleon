package ru.ibsqa.qualit.definitions.repository;

import java.util.List;

public interface IRepositoryVerifier {
    void verify(List<IRepositoryElement> elements);
}
