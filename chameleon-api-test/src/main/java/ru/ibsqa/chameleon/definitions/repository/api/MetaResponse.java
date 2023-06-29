package ru.ibsqa.chameleon.definitions.repository.api;

import ru.ibsqa.chameleon.definitions.repository.MetaPriority;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.json.utils.DataUtils;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ToString(exclude="pattern")
@XmlRootElement(name = "Response")
@MetaPriority(ConfigurationPriority.LOW)
public class MetaResponse extends AbstractInteraction {

    @Getter
    @XmlAttribute
    private String name;

    @Getter
    @XmlAttribute
    private ContentTypeEnum contentType;

    private final Pattern pattern = Pattern.compile("(\\d+).*");

    @XmlAttribute
    private String statusCode;

    public Integer getStatusCode() {
        if (null != statusCode && !statusCode.isEmpty()) {
            Matcher matcher = pattern.matcher(statusCode);
            if (matcher.matches()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return null;
    }

    @XmlAttribute
    private String schema;

    public String getSchema() {
        return DataUtils.getDataAsString(schema);
    }

    @Getter
    @XmlElement(name = "Header")
    private MetaHeader header;

    @Getter
    @XmlElement(name = "Body")
    private MetaBody body;

    @Getter
    @XmlElement(name = "Cookie")
    private MetaCookie cookie;

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isResponse() {
        return true;
    }
}
