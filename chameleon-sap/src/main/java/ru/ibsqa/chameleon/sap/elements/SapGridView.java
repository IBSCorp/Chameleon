package ru.ibsqa.chameleon.sap.elements;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;
import ru.ibsqa.chameleon.definitions.repository.ConfigurationPriority;
import ru.ibsqa.chameleon.elements.MetaElement;
import ru.ibsqa.chameleon.sap.definitions.repository.MetaSapGridView;
import ru.ibsqa.chameleon.sap.driver.SapSupportedDriver;
import ru.ibsqa.chameleon.utils.delay.DelayUtils;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@MetaElement(value = MetaSapGridView.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapGridView extends SapElementFacade {
    private List<String> keys;

    public void clickButton(String name) {
        ActiveXComponent grid = new ActiveXComponent(getVariant().getDispatch());
        int buttonCount = Integer.parseInt(grid.getProperty("ToolbarButtonCount").toString());
        for (int i = 0 ; i < buttonCount; i++){
            if (grid.invoke("GetToolbarButtonToolTip", i).toString().equals(name)){
                grid.invoke("pressToolbarButton", grid.invoke("GetToolbarButtonId", i));
                return;
            }
        }
    }

    public void clickContextButton(String name) {
        ActiveXComponent grid = new ActiveXComponent(getVariant().getDispatch());
        int buttonCount = Integer.parseInt(grid.getProperty("ToolbarButtonCount").toString());
        for (int i = 0 ; i < buttonCount; i++){
            if (grid.invoke("GetToolbarButtonToolTip", i).toString().equals(name)){
                grid.invoke("pressToolbarContextButton", grid.invoke("GetToolbarButtonId", i));
                return;
            }
        }
    }

    public void selectContextMenu(String name) {
        DelayUtils.sleep(3000);
        ActiveXComponent grid = new ActiveXComponent(getVariant().getDispatch());
        try {
            grid.invoke("SelectContextMenuItemByText", name);
        }catch (Exception e){
            e.printStackTrace();
            fail(String.format("При выборе пункта контекстного меню [%s] получена ошибка: [%s]", name, e.getMessage()));
        }
    }

    public boolean existRowWithParams(Map<String, String> params) {
        ActiveXComponent grid = new ActiveXComponent(getVariant().getDispatch());
        int rowCount = Integer.parseInt(grid.getProperty("RowCount").toString());
        boolean found = false;
        for (Map.Entry<String, String> row : params.entrySet()){
            found = false;
            for (int i = 0; i < rowCount; i++){
                Variant[] variants = new Variant[2];
                variants[0] = new Variant(i);
                variants[1] = new Variant(getColumnNameByTitle(row.getKey()));
                String cellValue = grid.invoke("GetCellValue", variants).toString();
                if (cellValue.matches(row.getValue())){
                    found = true;
                    break;
                }
            }
            if (!found){
                return false;
            }
        }
        return found;

    }

    public String getColumnNameByTitle(String title){
        ActiveXComponent grid = new ActiveXComponent(getVariant().getDispatch());
        int columnCount = Integer.parseInt(new ActiveXComponent(grid.getProperty("columnOrder").toDispatch()).invoke("count").toString());
        for (int i = 0; i < columnCount; i++){
            String currentTitle = new ActiveXComponent(grid.getProperty("columnOrder").toDispatch()).invoke("item", i).toString();
            if (grid.invoke("GetColumnToolTip", currentTitle).toString().contains(title) ||
                    grid.invoke("GetDisplayedColumnTitle", currentTitle).toString().equals(title)){
                return currentTitle;
            }
        }
        throw  new AssertionError("Не найдена колонка с заголовком: " + title);
    }

    public String getColumnNameByIndex(int index){
        ActiveXComponent grid = new ActiveXComponent(getVariant().getDispatch());
        return new ActiveXComponent(grid.getProperty("columnOrder").toDispatch()).invoke("item", index).toString();
    }

    public String getCellValue(String titleColumn, int row){
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(row - 1);
        variants[1] = new Variant(getColumnNameByTitle(titleColumn));
        return new ActiveXComponent(getVariant().getDispatch()).
                invoke("GetCellValue", variants).toString();
    }

    public String getCellValueByIndex(int columnIndex, int rowIndex) {
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(rowIndex - 1);
        variants[1] = new Variant(getColumnNameByIndex(columnIndex-1));
        return new ActiveXComponent(getVariant().getDispatch()).
                invoke("GetCellValue", variants).toString();
    }

    public void clickCell(String titleColumn, int row){
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(row - 1);
        variants[1] = new Variant(getColumnNameByTitle(titleColumn));
        new ActiveXComponent(getVariant().getDispatch()).
                invoke("Click", variants);
    }


}
