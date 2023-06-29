package ru.ibsqa.chameleon.definitions.repository;

import javax.annotation.PostConstruct;
import lombok.Setter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepositoryWrapperImpl implements IRepositoryWrapper {

    @Setter
    private String fileName;

    @Setter
    private ILoader loader;

    @Setter
    private IRepositoryData data;

    @PostConstruct
    public void initialization() {
        assertNotNull(loader);
        assertTrue(loader.load(this, fileName));
    }

    @Override
    public <ELEMENT extends IRepositoryElement> List<ELEMENT> pickAllElements() {
        assertNotNull(data);
        return data.pickAllElements();
    }

    @Override
    public <ELEMENT extends IRepositoryElement> ELEMENT pickElement(String name, Class<ELEMENT> elementType) {
        assertNotNull(data);
        return data.pickElement(name, elementType);
    }

}
