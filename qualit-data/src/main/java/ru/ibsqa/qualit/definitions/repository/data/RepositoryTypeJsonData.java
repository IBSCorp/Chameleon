package ru.ibsqa.qualit.definitions.repository.data;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.definitions.repository.IXmlRepositoryType;

@Component
public class RepositoryTypeJsonData implements IXmlRepositoryType {
    @Override
    public String getTypeName() {
        return "json-data";
    }

    @Override
    public Class<? extends IRepositoryElement> getAssignableFrom() {
        return IRepositoryElementData.class;
    }
}
