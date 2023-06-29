package ru.ibsqa.chameleon.definitions.repository.selenium;

import ru.ibsqa.chameleon.definitions.repository.IRepositoryData;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.definitions.repository.selenium.elements.MetaPage;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ToString
@XmlRootElement(name = "Pages")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaPages implements IRepositoryData, IRepositoryElement, IRepositoryElementSelenium {

    @XmlElement(name = "Page")
    private List<MetaPage> pages;

    private List<IMetaPage> getPages() {
        return pages.stream().map(p -> (IMetaPage) p).collect(Collectors.toList());
    }

    @Override
    public IMetaPage pickElement(String name, Class elementType) {
        return findElement(null, getPages(), name, elementType);
    }

    private IMetaPage findElement(IMetaPage parent, List<IMetaPage> pages, String name, Class elementType) {
        if (null == pages || MetaPage.class != elementType) {
            return null;
        }

        IMetaPage page = null;

        try {
            return pages
                    .stream()
                    .filter(item -> (null == name && null == item.getName()) || (null != item.getName() && item.getName().equals(name)))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            // Ищем вложенные страницы
            if (null == parent) {
                for (IMetaPage p : pages) {
                    page = findElement(p, p.getAllChildPages(), name, elementType);
                    if (null != page) {
                        break;
                    }
                }
            }
        }

        return page;
    }

    @Override
    public <E extends IRepositoryElement> List<E> pickAllElements() {
        List<E> pageList = new ArrayList<>((java.util.Collection<? extends E>) getPages());
        for (MetaPage page: pages){
            pageList.addAll((java.util.Collection<? extends E>) page.getAllChildPages());
        }
        return pageList;
    }
}
