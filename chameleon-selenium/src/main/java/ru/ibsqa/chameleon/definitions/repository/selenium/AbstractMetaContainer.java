package ru.ibsqa.chameleon.definitions.repository.selenium;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Элемент дерева, который может содержать внутри себя другие элементы (страница, коллекция)
 */
@ToString(callSuper = true)
public class AbstractMetaContainer extends AbstractMetaElement implements IMetaContainer {

    @XmlElementRef
    private List<AbstractMetaElement> elements;

    @Getter
    @XmlAttribute
    private int waitTimeOut = -1;

    public List<IMetaElement> getElements() {
        if (null == elements) {
            return new ArrayList<IMetaElement>();
        } else {
            return elements.stream()
                    .map(item -> (IMetaElement) item)
                    .collect(Collectors.toList());
        }
    }

    public List<IMetaField> getFields() {
        return getElements()
                .stream()
                .filter(item -> item instanceof IMetaField)
                .map(item -> (IMetaField) item)
                .collect(Collectors.toList());
    }

}
