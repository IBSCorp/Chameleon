package ru.ibsqa.qualit.sap.elements;

import ru.ibsqa.qualit.definitions.repository.ConfigurationPriority;
import ru.ibsqa.qualit.elements.MetaElement;
import ru.ibsqa.qualit.sap.definitions.repository.MetaSapTable;
import ru.ibsqa.qualit.sap.driver.SapDriver;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import ru.ibsqa.qualit.sap.driver.SapSupportedDriver;

import static org.junit.jupiter.api.Assertions.fail;

@MetaElement(value = MetaSapTable.class, supportedDriver = SapSupportedDriver.class, priority = ConfigurationPriority.LOW)
public class SapTable extends SapElementFacade {

    public void selectRow(String title, String value){
        for (int i = 1; i < getRowCount() + 1; i++) {
            if (getCellValue(title, i).equals(value)){
                doubleClickRow(i - 1);
                return;
            }
        }
        fail(String.format("Не найдена строка [%s] со значением [%s]", title, value));
    }
    public String getCellValue(String title, int row){
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(row - 1);
        variants[1] = new Variant(getColumnName(title));
        return new ActiveXComponent(getVariant().getDispatch()).
                        invoke("GetCellValue", variants).toString();
    }

    public String getCellValueByRowName(String titleRow, int column){
        this.isDisplayed();
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(getRowIndexByName(titleRow));
        variants[1] = new Variant(getColumnNameByIndex(column-1));
        return new ActiveXComponent(((SapDriver) getDriver().getWrappedDriver()).getVariant().getDispatch()).
                invoke("GetCellValue", variants).toString();
    }

    private String getColumnName(String title){
        ActiveXComponent activeXComponent = new ActiveXComponent(getVariant().getDispatch());
        int rowCount = getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            String columnName = new ActiveXComponent(activeXComponent.getProperty("ColumnOrder").toDispatch()).invoke("ElementAt", i).getString();
            String columnTitle = activeXComponent.invoke("GetColumnTooltip", columnName).getString().replaceAll("\\s+", " ").trim();
            if (title.equals(columnTitle)){
                return columnName;
            }
        }
        return null;
    }
    private String getColumnNameByIndex(int index){
        ActiveXComponent activeXComponent = new ActiveXComponent(getVariant().getDispatch());
        return  new ActiveXComponent(activeXComponent.getProperty("ColumnOrder").toDispatch()).invoke("ElementAt", index).getString();
    }
    private int getRowCount(){
        return  new ActiveXComponent(getVariant().getDispatch()).getProperty("RowCount").getInt();
    }
    private int getColumnCount(){
        return  new ActiveXComponent(getVariant().getDispatch()).getProperty("ColumnCount").getInt();
    }

    public void doubleClickRow(int row){
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(row);
        variants[1] = new Variant(getColumnNameByIndex(1));
        new ActiveXComponent(getVariant().getDispatch()).invoke("DoubleClick",variants);
    }

    public void doubleClickCell(String column, int row){
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(row-1);
        variants[1] = new Variant(getColumnName(column));
        new ActiveXComponent(getVariant().getDispatch()).invoke("DoubleClick",variants);
    }

    public void clickCell(String column, int row){
        Variant[] variants = new Variant[2];
        variants[0] = new Variant(row-1);
        variants[1] = new Variant(getColumnName(column));
        new ActiveXComponent(getVariant().getDispatch()).invoke("Click",variants);
    }

    public void setCellValue(String column, String row, String value) {
        Variant[] variants = new Variant[3];
        variants[0] = new Variant(getRowIndexByName(row));
        variants[1] = new Variant(getColumnNameByIndex(Integer.parseInt(column)-1));
        variants[2] = new Variant(value);
        new ActiveXComponent(getVariant().getDispatch()).
                invoke("modifyCell", variants);
    }

    private Integer getRowIndexByName(String name){
        ActiveXComponent activeXComponent = new ActiveXComponent(getVariant().getDispatch());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                Variant[] variants = new Variant[2];
                variants[0] = new Variant(i);
                variants[1] = new Variant(getColumnNameByIndex(j));
                if(activeXComponent.invoke("GetCellValue", variants).toString().equals(name)){
                    return i;
                }
            }
        }
        return null;
    }

    @Override
    public void click(){

    }

    public void clickButton(String buttonName) {
        ActiveXComponent activeXComponent = new ActiveXComponent(getVariant().getDispatch());
        activeXComponent.invoke("pressButton", buttonName);
    }
}
