package ru.ibsqa.chameleon.definitions.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.selenium.templates.MetaTemplate;

@Component
public class LocatorCreatorImpl implements ILocatorCreator {

    @Autowired
    private IRepositoryManager repositoryManager;

    @Autowired
    private ITemplateParamsResolver templateParamsResolver;

    @Override
    public String createLocator(String template, String name) {
        return String.format(
                repositoryManager.pickElement(template, MetaTemplate.class).getLocator(),
                templateParamsResolver.getParams(name)
        );
    }

}
