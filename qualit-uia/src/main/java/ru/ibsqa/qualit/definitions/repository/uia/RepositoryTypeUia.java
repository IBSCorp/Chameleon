package ru.ibsqa.qualit.definitions.repository.uia;

import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.definitions.repository.IXmlRepositoryType;
import ru.ibsqa.qualit.definitions.repository.selenium.IRepositoryElementSelenium;

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
