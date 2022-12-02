package ru.ibsqa.qualit.definitions.repository.data;

import org.junit.jupiter.api.Assertions;
import ru.ibsqa.qualit.context.IContextExplorer;
import ru.ibsqa.qualit.definitions.repository.IRepositoryElement;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.fail;

@ToString(exclude={"fieldList"})
public abstract class AbstractDataMetaField implements IRepositoryElement {

    IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

    @Getter
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String locator;

    public String getLocator() {
        return evaluateManager.evalVariable(SpringUtils.getBean(IContextExplorer.class).getPickElementScope(), locator);
    }

    @Getter
    @XmlAttribute
    private boolean inPath;

    @Getter
    @XmlAttribute
    private String defaultValue;

    @Getter
    private AbstractDataMetaFieldList fieldList;

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        this.fieldList = (AbstractDataMetaFieldList)parent;
    }

    protected abstract Class getPrimitiveType();

    /**
     * Получить из строки типизированное значение
     * @param value
     * @return
     */
    public Object parseValue(String value) {

        try {
            if (Long.class == getPrimitiveType()) {
                return Long.parseLong(value);
            } else if (BigDecimal.class == getPrimitiveType()) {
                return new BigDecimal(value);
            } else if (Boolean.class == getPrimitiveType()) {
                return Boolean.parseBoolean(value);
            }
        } catch (NumberFormatException e1) {
            Assertions.fail(ILocaleManager.message("numberFormatErrorMessage"));
        }

        return value;
    }

}
