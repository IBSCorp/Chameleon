package ru.ibsqa.chameleon.definitions.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaContainer;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaElement;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaField;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaCollection;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaPage;
import ru.ibsqa.chameleon.i18n.ILocaleManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

@Component
public class RepositoryVerifierSeleniumImpl implements IRepositoryVerifier {

    private Map<String, MetaPage> pagesMap = new HashMap<>();

    @Autowired
    private ILocaleManager localeManager;

    @Override
    public void verify(List<IRepositoryElement> elements) {
        pagesMap.clear();
        for (IRepositoryElement el : elements) {
            if (el instanceof MetaPage) {
                MetaPage page = (MetaPage) el;
                checkPageUnique(page);
            }
        }
        for (IRepositoryElement el : elements) {
            if (el instanceof MetaPage) {
                MetaPage page = (MetaPage) el;
                checkFieldsUnique(page);
                page.getCollections().forEach(collection -> checkFieldsUnique((AbstractMetaContainer) collection));
            }
        }
    }

    private void checkPageUnique(MetaPage page) {
        if (Objects.nonNull(pagesMap.get(page.getName()))) {
            fail(localeManager.getMessage("nonUniquePage", page.getName()));
        }
        pagesMap.put(page.getName(), page);
    }

    private void checkFieldsUnique(AbstractMetaContainer container) {
        List<IMetaField> fields = container.getFields();
        if (container instanceof MetaPage) {
            MetaPage page = (MetaPage) container;
            fields.addAll(
                    page.getBlocks()
                            .stream()
                            .map(block -> pagesMap.get(block.getName()))
                            .filter(Objects::nonNull)
                            .flatMap(blockPage -> blockPage.getFields().stream())
                            .collect(Collectors.toList())
            );
        }

        List<String> nonUniqueFieldNames = fields.stream()
                .collect(Collectors.groupingBy(IMetaElement::getName, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (nonUniqueFieldNames.size() > 0) {
            fail(localeManager.getMessage((container instanceof MetaCollection) ? "nonUniqueFieldInCollection" : "nonUniqueFieldInPage",
                    nonUniqueFieldNames.get(0), container.getName()));
        }
    }

}
