package ru.ibsqa.qualit.sap.elements;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.sap.definitions.repository.MetaSapTreeTable;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;
import com.jacob.activeX.ActiveXComponent;
import lombok.extern.slf4j.Slf4j;
import ru.ibsqa.qualit.utils.delay.DelayUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaSapTreeTable.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapTreeTable extends SapElementFacade {
    private List<String> keys;

    public void doubleClickNode(String value) {
        if (keys == null){
            keys = getKeys();
        }
        for (String key : keys) {
            DelayUtils.sleepSec(1);
            if (getNodeValue(key).equals(value)) {
                doubleClick(key);
                return;
            }
        }
        fail(String.format("Не найдена строка со значением [%s]", value));
    }

    public void selectNode(String value) {
        if (keys == null){
            keys = getKeys();
        }
        for (String key : keys) {
            DelayUtils.sleepSec(1);
            if (getNodeValue(key).equals(value)) {
                select(key);
                return;
            }
        }
        fail(String.format("Не найдена строка со значением [%s]", value));
    }

    public void expandNode(String value) {
        if (keys == null){
            keys = getKeys();
        }
        for (String key : keys) {
            DelayUtils.sleepSec(1);
            if (getNodeValue(key).equals(value)) {
                expand(key);
                return;
            }
        }
        fail(String.format("Не найдена строка со значением [%s]", value));
    }

    public String getNodeValue(String key) {
        ActiveXComponent mActiveXComponent = new ActiveXComponent((getVariant().getDispatch()));
        String nodeValue = mActiveXComponent.invoke("GetNodeTextByKey", key).getString();
        log.info(nodeValue);
        return nodeValue;
    }

    private int getRowCount() {
        ActiveXComponent mActiveXComponent = new ActiveXComponent((getVariant().getDispatch()));
        return new ActiveXComponent(mActiveXComponent.invoke("GetAllNodeKeys").toDispatch()).getProperty("count").getInt();
    }

    private List<String> getKeys() {
        keys = new ArrayList<>();
        ActiveXComponent mActiveXComponent = new ActiveXComponent((getVariant().getDispatch()));
        DelayUtils.sleepSec(1);
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            keys.add(new ActiveXComponent(mActiveXComponent.invoke("GetAllNodeKeys").toDispatch()).invoke("ElementAt", i).getString());
        }
        return keys;
    }

    public void doubleClick(String key) {
        ActiveXComponent mActiveXComponent = new ActiveXComponent((getVariant().getDispatch()));
        mActiveXComponent.invoke("doubleClickNode", key);
    }

    protected void expand(String key) {
        ActiveXComponent mActiveXComponent = new ActiveXComponent((getVariant().getDispatch()));
        mActiveXComponent.invoke("ExpandNode", key);
    }

    protected void select(String key) {
        ActiveXComponent mActiveXComponent = new ActiveXComponent((getVariant().getDispatch()));
        mActiveXComponent.setProperty("selectedNode", key);
    }

    public void selectByPath(String path) {
        String[] paths = path.split(">");
        for (int i = 0; i < paths.length; i++) {
            if (i + 1 == paths.length) {
                doubleClickNode(paths[i]);
            } else {
                expandNode(paths[i]);
            }
        }
    }

}
