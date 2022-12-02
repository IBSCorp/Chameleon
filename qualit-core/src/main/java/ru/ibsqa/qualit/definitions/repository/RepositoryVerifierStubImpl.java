package ru.ibsqa.qualit.definitions.repository;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepositoryVerifierStubImpl implements IRepositoryVerifier {

    @Override
    public void verify(List<IRepositoryElement> elements) {
    }

}
