package ru.ibsqa.chameleon.definitions.repository.selenium.templates;

import ru.ibsqa.chameleon.definitions.repository.IRepositoryData;
import ru.ibsqa.chameleon.definitions.repository.IRepositoryElement;
import lombok.Getter;
import lombok.ToString;
import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@ToString
@XmlRootElement(name = "Templates")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaTemplates implements IRepositoryData, IRepositoryElementTemplate {

    @Getter
    @XmlElement(name = "Template")
    private List<MetaTemplate> templates;

    @Override
    public MetaTemplate pickElement(String name, Class elementType) {
        return findElement(templates, name, elementType);
    }

    private MetaTemplate findElement(List<MetaTemplate> templates, String name, Class elementType) {
        if (null == templates) {
            return null;
        }
        return getTemplates().stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public <E extends IRepositoryElement> List<E> pickAllElements() {
        return (List<E>) getTemplates();
    }
}
