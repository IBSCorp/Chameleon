package ru.ibsqa.qualit.definitions.repository.db;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.definitions.repository.IXmlRepositoryType;

@Component
public class RepositoryTypeDb implements IXmlRepositoryType {
    @Override
    public String getTypeName() {
        return "db";
    }

    @Override
    public Class<? extends IRepositoryElement> getAssignableFrom() {
        return IRepositoryElementDb.class;
    }
}
