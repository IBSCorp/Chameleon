package ru.ibsqa.qualit.sap.elements;

import com.jacob.activeX.ActiveXComponent;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.sap.definitions.repository.MetaSapStaticText;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;

@MetaElement(value = MetaSapStaticText.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapStaticText extends SapElementFacade {

    @Override
    public void click(){
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());
        mActiveXComponent.invoke("setFocus");
    }

}
