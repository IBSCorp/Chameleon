package ru.ibsqa.chameleon.definitions.repository.winium;

import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.chameleon.definitions.repository.selenium.IRepositoryElementSelenium;

@Component
public class RepositoryTypeWinium implements IXmlRepositoryType {
    @Override
    public String getTypeName() {
        return "winium-elements";
    }

    @Override
    public Class<? extends IRepositoryElement> getAssignableFrom() {
        return IRepositoryElementSelenium.class;
    }

}
