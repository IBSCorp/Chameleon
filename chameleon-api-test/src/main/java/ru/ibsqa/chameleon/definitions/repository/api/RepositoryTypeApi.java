package ru.ibsqa.chameleon.definitions.repository.api;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.IXmlRepositoryType;

@Component
public class RepositoryTypeApi implements IXmlRepositoryType {
    @Override
    public String getTypeName() {
        return "api";
    }

    @Override
    public Class<? extends IRepositoryElement> getAssignableFrom() {
        return IRepositoryElementApi.class;
    }
}
