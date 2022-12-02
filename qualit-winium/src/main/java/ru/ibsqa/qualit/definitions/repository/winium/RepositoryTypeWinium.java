package ru.ibsqa.qualit.definitions.repository.winium;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.qualit.definitions.repository.selenium.IRepositoryElementSelenium;

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
