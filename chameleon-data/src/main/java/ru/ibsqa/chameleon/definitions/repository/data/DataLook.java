package ru.ibsqa.chameleon.definitions.repository.data;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.json.utils.DataUtils;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@ToString(callSuper = true)
@XmlRootElement(name="DataLook")
@MetaPriority(ConfigurationPriority.LOW)
public class DataLook extends AbstractDataMetaFieldList {

    @Getter
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String schema;

    public String getSchema() {
        return DataUtils.getDataAsString(schema);
    }
}
