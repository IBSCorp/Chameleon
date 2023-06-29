package ru.ibsqa.chameleon.definitions.repository.api;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.json.utils.DataUtils;
import io.restassured.http.Method;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ToString
@XmlRootElement(name = "Request")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaRequest extends AbstractInteraction {

    @Getter
    @XmlAttribute
    private String name;

    @Getter
    @XmlAttribute
    private Method method;

    @XmlAttribute
    private String template;

    @Getter
    @XmlAttribute
    private ContentTypeEnum contentType;

    public String getTemplate() {
        return DataUtils.getDataAsString(template);
    }

    @Getter
    @XmlElement(name = "Params")
    private MetaParams params;

    @Getter
    @XmlElement(name = "Header")
    private MetaHeader header;

    @Getter
    @XmlElement(name = "Cookie")
    private MetaCookie cookie;

    @Getter
    @XmlElement(name = "Body")
    private MetaBody body;

    @Getter
    @XmlAttribute
    private String credential;

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return false;
    }
}
