package ru.ibsqa.qualit.page_factory.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
public class CollectionSuperClassManagerImpl implements ICollectionSuperClassManager {

    private List<ICollectionSuperClassResolver> resolvers;

    @Autowired
    private ILocaleManager localeManager;

    @Autowired
    private void collectResolvers(List<ICollectionSuperClassResolver> resolvers) {
        this.resolvers = resolvers;
        this.resolvers.sort(Comparator.comparing(ICollectionSuperClassResolver::getPriority));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IPageObject> Class<T> getSuperClass(IMetaCollection metaCollection, WebDriverFacade webDriverFacade) {
        return resolvers.stream()
                .map(r -> (Class<T>) r.getSuperClass(metaCollection, webDriverFacade))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(localeManager.getMessage("collectionFacadeNotFound", metaCollection.getClass().getCanonicalName())));
    }
}
