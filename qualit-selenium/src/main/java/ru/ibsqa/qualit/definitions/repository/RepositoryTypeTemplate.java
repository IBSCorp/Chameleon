package ru.ibsqa.qualit.definitions.repository;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.selenium.templates.IRepositoryElementTemplate;

@Component
public class RepositoryTypeTemplate implements IXmlRepositoryType {
    @Override
    public String getTypeName() {
        return "templates";
    }

    @Override
    public Class<? extends IRepositoryElement> getAssignableFrom() {
        return IRepositoryElementTemplate.class;
    }

}
