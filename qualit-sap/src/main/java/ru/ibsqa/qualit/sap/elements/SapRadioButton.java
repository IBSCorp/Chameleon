package ru.ibsqa.qualit.sap.elements;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.sap.definitions.repository.MetaSapRadioButton;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;

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
