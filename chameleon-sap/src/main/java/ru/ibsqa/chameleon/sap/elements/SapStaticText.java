package ru.ibsqa.chameleon.sap.elements;

import com.jacob.activeX.ActiveXComponent;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.sap.definitions.repository.MetaSapStaticText;
import ru.ibsqa.chameleon.sap.driver.SapSupportedDriver;

@MetaElement(value = MetaSapStaticText.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapStaticText extends SapElementFacade {

    @Override
    public void click(){
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());
        mActiveXComponent.invoke("setFocus");
    }

}
