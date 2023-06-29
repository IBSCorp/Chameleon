package ru.ibsqa.chameleon.definitions.repository.selenium.elements;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.AbstractMetaContainer;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaBlock;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaCollection;
import ru.ibsqa.chameleon.definitions.repository.selenium.IMetaPage;
import ru.ibsqa.chameleon.definitions.repository.selenium.adapters.AdapterPage;
import ru.ibsqa.chameleon.page_factory.pages.IPageObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString(callSuper = true)
@XmlRootElement(name = "Page")
@XmlJavaTypeAdapter(AdapterPage.class)
@MetaPriority(ConfigurationPriority.LOW)
public class MetaPage extends AbstractMetaContainer implements IMetaPage {

    @Getter @Setter
    private Class<IPageObject> pageObjectClass;

    @Getter
    @XmlAttribute
    private String driver;

    @Getter
    @XmlAttribute
    private String customType;

    private List<IMetaPage> getPages() {
        return getElements()
                .stream()
                .filter(item -> item instanceof IMetaPage)
                .map(item -> (IMetaPage) item)
                .collect(Collectors.toList());
    }

    public List<IMetaBlock> getBlocks() {
        return getElements()
                .stream()
                .filter(item -> item instanceof MetaBlock)
                .map(item -> (MetaBlock)item)
                .collect(Collectors.toList());
    }

    public List<IMetaPage> getAllChildPages() {
        List<IMetaPage> allPages = new ArrayList<>(getPages());
        for (IMetaPage page : getPages()) {
            allPages.addAll(page.getAllChildPages());
        }
        return allPages;
    }

    public List<IMetaCollection> getCollections() {
        return getElements()
                .stream()
                .filter(item -> item instanceof IMetaCollection)
                .map(item -> (IMetaCollection) item)
                .collect(Collectors.toList());
    }

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        //генерация PageObject классов на основе страницы их XML

    }
}
