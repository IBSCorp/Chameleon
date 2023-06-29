package ru.ibsqa.chameleon.sap.elements;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.sap.definitions.repository.MetaSapRadioButton;
import ru.ibsqa.chameleon.sap.driver.SapSupportedDriver;

@MetaElement(value = MetaSapRadioButton.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapRadioButton extends SapElementFacade {

    @Override
    public void type(String value){
        Variant variant = getVariant();
        ActiveXComponent mActiveXComponent = new ActiveXComponent(variant.getDispatch());
        int count = Integer.parseInt(mActiveXComponent.getProperty("VisibleRowCount").toString());
        for (int i = 0; i < count; i ++){
            try{
                ActiveXComponent row = new ActiveXComponent(mActiveXComponent.invoke("getAbsoluteRow", i).toDispatch());
                if ( new ActiveXComponent(row.invoke("ElementAt", 0).toDispatch()).getProperty("text").toString().equals(value)){
                    row.setProperty("selected", -1);
                    break;
                }
            }catch (Exception e){

            }

        }
   }

}
