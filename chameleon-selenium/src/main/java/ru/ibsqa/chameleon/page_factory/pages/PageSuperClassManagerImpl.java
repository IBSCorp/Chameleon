package ru.ibsqa.chameleon.page_factory.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaPage;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
public class PageSuperClassManagerImpl implements IPageSuperClassManager {

    private List<IPageSuperClassResolver> resolvers;

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private void collectResolvers(List<IPageSuperClassResolver> resolvers) {
        this.resolvers = resolvers;
        this.resolvers.sort(Comparator.comparing(IPageSuperClassResolver::getPriority));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IPageObject> Class<T> getSuperClass(IMetaPage metaPage, IDriverFacade driverFacade) {
        return resolvers.stream()
                .map(r -> (Class<T>) r.getSuperClass(metaPage, driverFacade))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(localeManager.getMessage("pageFacadeNotFound", metaPage.getClass().getCanonicalName())));
    }
}
