package ru.ibsqa.qualit.definitions.repository.selenium;

import ru.ibsqa.qualit.definitions.repository.IElementFacadeMapper;
import ru.ibsqa.qualit.selenium.driver.ISupportedDriver;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Общий класс для всех полей
 */
@ToString(callSuper = true)
public abstract class AbstractMetaField extends AbstractMetaElement implements IMetaField {

    @Getter
    @XmlAttribute
    private String customType;

    @Getter
    @XmlAttribute
    private boolean isLoaded = false;

    @Getter
    @XmlAttribute
    private int waitTimeOut = -1;

    public String getFacadeClassName(Class<? extends ISupportedDriver> supportedDriver) {
        if (null != customType && !customType.isEmpty()) {
            return customType;
        } else {
            return getElementFacadeMapper().getFacadeClassName(this.getClass(), supportedDriver);
        }
    }

    private static IElementFacadeMapper elementFacadeMapper;
    private static IElementFacadeMapper getElementFacadeMapper(){
        if (null == elementFacadeMapper){
            elementFacadeMapper = SpringUtils.getBean(IElementFacadeMapper.class);
        }
        return elementFacadeMapper;
    }

    protected String getFacadePackageName() {
        return "";
    }

}
