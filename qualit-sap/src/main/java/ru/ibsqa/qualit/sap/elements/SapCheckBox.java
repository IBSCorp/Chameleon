package ru.ibsqa.qualit.sap.elements;

import com.jacob.activeX.ActiveXComponent;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.sap.definitions.repository.MetaSapCheckBox;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;

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
