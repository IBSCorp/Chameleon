package ru.ibsqa.chameleon.definitions.repository.web;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.chameleon.definitions.repository.selenium.IRepositoryElementSelenium;

@Component
public class RepositoryTypeWeb implements IXmlRepositoryType {
    @Override
    public String getTypeName() {
        return "web-elements";
    }

    @Override
    public Class<? extends IRepositoryElement> getAssignableFrom() {
        return IRepositoryElementSelenium.class;
    }

}
