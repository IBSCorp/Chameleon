package ru.ibsqa.chameleon.page_factory.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.chameleon.i18n.ILocaleManager;
import ru.ibsqa.chameleon.selenium.driver.IDriverFacade;

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
    public <T extends IPageObject> Class<T> getSuperClass(IMetaCollection metaCollection, IDriverFacade driverFacade) {
        return resolvers.stream()
                .map(r -> (Class<T>) r.getSuperClass(metaCollection, driverFacade))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(localeManager.getMessage("collectionFacadeNotFound", metaCollection.getClass().getCanonicalName())));
    }
}
