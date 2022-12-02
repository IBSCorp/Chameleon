package ru.ibsqa.qualit.definitions.repository.selenium.templates;

import lombok.Getter;
import lombok.ToString;
import ru.ibsqa.qualit.definitions.repository.MetaPriority;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name = "Template")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaTemplate implements IRepositoryElementTemplate {

    @Getter
    @XmlAttribute
    private String name;

    @Getter
    @XmlAttribute
    private String locator;

}
