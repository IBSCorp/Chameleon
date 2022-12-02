package ru.ibsqa.qualit.definitions.repository.selenium;

import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
import ru.ibsqa.qualit.definitions.repository.ITemplateParamsResolver;
import ru.ibsqa.qualit.definitions.repository.selenium.templates.MetaTemplate;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.NoSuchElementException;
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

    public String getLocator(){
        String tmlt = "";
        if (null == locator){
            if (null == template){
                if (this instanceof IMetaField) {
                    //TODO если не указали локатор и не найден в репозитории темплейтов, то выводить более информативное сообщение
                    //fail("У элемента " + getName() + " не указан ни локатор, ни шаблон");
                    tmlt = this.getClass().getSimpleName().replace("Meta", "");
                } else {
                    return null;
                }
            } else {
                tmlt = template;
            }
            try {
                ITemplateParamsResolver templateParamsResolver = SpringUtils.getBean(ITemplateParamsResolver.class);
                return String.format(getRepositoryManager().pickElement(tmlt, MetaTemplate.class).getLocator(), templateParamsResolver.getParams(name));
            } catch (NoSuchElementException e) {
                return null;
            }
        }
        return locator;
    }

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof AbstractMetaContainer){
            this.parent = (AbstractMetaContainer) parent;
        }
    }

    private IRepositoryManager getRepositoryManager(){
        if (null == repositoryManager){
            repositoryManager = SpringUtils.getBean(IRepositoryManager.class);
        }
        return repositoryManager;
    }

}
