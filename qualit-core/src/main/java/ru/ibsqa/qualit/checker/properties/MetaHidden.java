package ru.ibsqa.qualit.checker.properties;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Hidden")
@Slf4j
public class MetaHidden extends AbstractMetaProperty {

    @Override
    public PropertyType getType() {
        return PropertyType.Hidden;
    }

}
