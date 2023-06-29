package ru.ibsqa.chameleon.sap.elements;

import com.jacob.activeX.ActiveXComponent;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.sap.definitions.repository.MetaSapCheckBox;
import ru.ibsqa.chameleon.sap.driver.SapSupportedDriver;

@MetaElement(value = MetaSapCheckBox.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapCheckBox extends SapElementFacade {

    @Override
    public void type(String value){
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());
        if (Boolean.parseBoolean(value)){
            mActiveXComponent.setProperty("selected", 1);
        }else {
            mActiveXComponent.setProperty("selected", 0);
        }
    }

}
