package ru.ibsqa.qualit.definitions.repository;

import ru.ibsqa.qualit.definitions.repository.selenium.AbstractMetaContainer;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaElement;
import ru.ibsqa.qualit.definitions.repository.selenium.IMetaField;
import ru.ibsqa.qualit.definitions.repository.selenium.elements.MetaCollection;
import ru.ibsqa.qualit.definitions.repository.selenium.elements.MetaPage;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                checkPageUniquie(page);
            }
        }
        for (IRepositoryElement el : elements) {
            if (el instanceof MetaPage) {
                MetaPage page = (MetaPage) el;
                checkFieldsUniquie(page);
                page.getCollections().forEach(collection -> checkFieldsUniquie((AbstractMetaContainer) collection));
            }
        }
    }

    private void checkPageUniquie(MetaPage page) {
        if (Objects.nonNull(pagesMap.get(page.getName()))) {
            Assertions.fail(localeManager.getMessage("nonUniquiePage", page.getName()));
        }
        pagesMap.put(page.getName(), page);
    }

    private void checkFieldsUniquie(AbstractMetaContainer container) {
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

        List<String> nonUniquieFieldNames = fields.stream()
                .collect(Collectors.groupingBy(IMetaElement::getName, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (nonUniquieFieldNames.size() > 0) {
            fail(localeManager.getMessage((container instanceof MetaCollection) ? "nonUniquieFieldInCollection" : "nonUniquieFieldInPage", nonUniquieFieldNames.get(0), container.getName()));
        }
    }

}
