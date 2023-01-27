package ru.ibsqa.qualit.definitions.repository.selenium;

import ru.ibsqa.qualit.definitions.repository.ILocatorCreator;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Любой элемент в дереве: страница, коллекция, поля
 */
@ToString(exclude={"parent"})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractMetaElement implements IRepositoryElement, IMetaElement {

    private static IRepositoryManager repositoryManager = null;

    @Getter
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String locator;

    @Getter
    @XmlAttribute
    private String template;

    @XmlAttribute
    private String frames;

    public String[] getFrames() {
        return Optional.ofNullable(frames)
                .map(f -> f.split("\\>"))
                .map(a -> Stream.of(a)
                        .map(String::trim)
                        .toArray(String[]::new))
                .orElse(new String[0]);
    }

    private AbstractMetaContainer parent;

    public IMetaContainer getParent() {
        return this.parent;
    }

    public String getLocator() {
        if (Objects.nonNull(locator)) {
            return locator;
        }

        String template = getTemplate();
        if (Objects.isNull(getTemplate())) {
            if (this instanceof IMetaField) {
                template = this.getClass().getSimpleName().replace("Meta", "");
            } else {
                return null;
            }
        }

        try {
            return SpringUtils.getBean(ILocatorCreator.class).createLocator(template, name);
        } catch (NoSuchElementException e) {
            return null;
        }

    }

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof AbstractMetaContainer){
            this.parent = (AbstractMetaContainer) parent;
        }
    }

}
