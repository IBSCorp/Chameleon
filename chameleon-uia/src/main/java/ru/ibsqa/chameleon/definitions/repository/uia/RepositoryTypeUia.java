package ru.ibsqa.chameleon.definitions.repository.uia;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.chameleon.definitions.repository.selenium.IRepositoryElementSelenium;

@Component
public class RepositoryTypeUia implements IXmlRepositoryType {
    @Override
    public String getTypeName() {
        return "uia-elements";
    }

    @Override
    public Class<? extends IRepositoryElement> getAssignableFrom() {
        return IRepositoryElementSelenium.class;
    }

}
