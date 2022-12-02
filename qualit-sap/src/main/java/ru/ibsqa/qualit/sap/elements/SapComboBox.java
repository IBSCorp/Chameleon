package ru.ibsqa.qualit.sap.elements;

import com.jacob.activeX.ActiveXComponent;
import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.sap.definitions.repository.MetaSapComboBox;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

@MetaElement(value = MetaSapComboBox.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapComboBox extends SapElementFacade {

    @Override
    public void type(String type) {
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());
        for (int i = 0; i < getItemsCount(); i++) {
            ActiveXComponent currentElement = new ActiveXComponent(new ActiveXComponent(mActiveXComponent.getProperty("Entries").toDispatch()).invoke("ElementAt", i).
                    toDispatch());
            String value = currentElement.getProperty("value").getString();
            String key = currentElement.getProperty("key").getString();
            if (value.equals(type)) {
                mActiveXComponent.setProperty("key", key);
                return;
            }
        }
        fail("В выпадающем списке не найдено значение: " + type);
    }

    public Map<String, String> getItems() {
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());
        Map<String, String> items = new LinkedHashMap<>();
        for (int i = 0; i < getItemsCount(); i++) {
            String value = new ActiveXComponent(new ActiveXComponent(mActiveXComponent.getProperty("Entries").toDispatch()).invoke("ElementAt", i).
                    toDispatch()).getProperty("value").getString();
            String key = new ActiveXComponent(new ActiveXComponent(mActiveXComponent.getProperty("Entries").toDispatch()).invoke("ElementAt", i).toDispatch()).
                    getProperty("key").getString();
            items.put(key, value);
        }
        return items;
    }

    private int getItemsCount() {
        ActiveXComponent mActiveXComponent = new ActiveXComponent(getVariant().getDispatch());
    //    new ActiveXComponent(mActiveXComponent.getProperty("Entries").toDispatch()).getProperty("Count").getInt();
        return new ActiveXComponent(mActiveXComponent.getProperty("Entries").toDispatch()).getProperty("Count").getInt();
    }
}
